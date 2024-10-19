package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.IngredientRepository;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IngredientService {

    @Qualifier("ingredientRepository")
    private final IngredientRepository ingredientRepository;
    private final LambdaServiceClient lambdaServiceClient;
    @Qualifier("taskExecutor")
    private final TaskExecutor taskExecutor;


    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, LambdaServiceClient lambdaServiceClient,
                             @Qualifier("taskExecutor")TaskExecutor taskExecutor) {
        this.ingredientRepository = ingredientRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.taskExecutor = taskExecutor;
    }

    @Async
    public CompletableFuture<IngredientResponse> addIngredient(IngredientCreateRequest ingredientCreateRequest) throws IOException {
        return lambdaServiceClient.addIngredient(ingredientCreateRequest).thenApply(ingredientResponse -> {
            IngredientRecord ingredientRecord = new IngredientRecord(
                    ingredientResponse.getId(),
                    ingredientResponse.getName(),
                    ingredientResponse.getQuantity()
            );
            ingredientRepository.save(ingredientRecord);
            return ingredientResponse;
        });
    }

    @Async
    public CompletableFuture<IngredientResponse> getIngredientById(String id) {
        return ingredientRepository.findById(id)
                .map(record ->  CompletableFuture.completedFuture(new IngredientResponse(
                        record.getId(),
                        record.getName(),
                        record.getQuantity()
                )))
                .orElseGet(() -> {
                    try {
                        return lambdaServiceClient.getIngredientById(id);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Async
    public CompletableFuture<List<IngredientResponse>> getAllIngredients() {
        return CompletableFuture.supplyAsync(() -> {
            List<IngredientRecord> ingredientRecords = StreamSupport
                    .stream(ingredientRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            if (!ingredientRecords.isEmpty()) {
                return ingredientRecords.stream()
                        .map(record -> new IngredientResponse(record.getId(),
                                record.getName(),
                                record.getQuantity()))
                        .collect(Collectors.toList());
            }

            return null;
        }, taskExecutor).thenCompose(localResult -> {
            if (localResult == null) {
                try {
                    return lambdaServiceClient.getAllIngredients()
                            .thenApply(List::of)
                            .exceptionally(ex -> {
                                System.err.println("Error fetching ingredients from Lambda: " + ex.getMessage());
                                return List.of();
                            });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return CompletableFuture.completedFuture(localResult);
        });
    }

    @Async
    public CompletableFuture<IngredientResponse> updateIngredient(String ingredientId, IngredientUpdateRequest ingredientUpdateRequest) {
        try {
            return lambdaServiceClient.updateIngredient(ingredientId, ingredientUpdateRequest).thenApply(ingredientResponse -> {
                IngredientRecord ingredientRecord = new IngredientRecord(
                        ingredientResponse.getId(),
                        ingredientResponse.getName(),
                        ingredientResponse.getQuantity()
                );
                ingredientRepository.save(ingredientRecord);
                return ingredientResponse;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public CompletableFuture<DeleteIngredientResponse> deleteIngredient(String ingredientId) {
        try {
            return lambdaServiceClient.deleteIngredientById(ingredientId).thenApply(deleteResponse -> {
                if (deleteResponse != null && deleteResponse.getId() != null) {
                    ingredientRepository.deleteById(deleteResponse.getId());
                }
                return deleteResponse;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Qualifier("TaskScheduler-")
    public CompletableFuture<Void> checkAndReplenishStock() {
        List<IngredientRecord> ingredients = (List<IngredientRecord>) ingredientRepository.findAll();

        for (IngredientRecord ingredient : ingredients) {
            if (ingredient.getQuantity() < 10) {
                System.out.println("Low stock detected for: " + ingredient.getName() + ". Replenishing...");
                replenishStock(ingredient);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    private void replenishStock(IngredientRecord ingredientRecord) {
        ingredientRecord.setQuantity(ingredientRecord.getQuantity() + 100);
        ingredientRepository.save(ingredientRecord);
        System.out.println("Replenished stock for ingredient: " + ingredientRecord.getName());
    }
}
