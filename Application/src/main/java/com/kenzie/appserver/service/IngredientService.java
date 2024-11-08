package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.IngredientRepository;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public CompletableFuture<IngredientResponse> addIngredient(IngredientCreateRequest ingredientCreateRequest) {
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
                .orElseGet(() -> lambdaServiceClient.getIngredientById(id));
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
                return lambdaServiceClient.getAllIngredients()
                        .thenApply(List::of)
                        .exceptionally(ex -> {
                            System.err.println("Error fetching ingredients from Lambda: " + ex.getMessage());
                            return List.of();
                        });
            }
            return CompletableFuture.completedFuture(localResult);
        });
    }

    @Async
    public CompletableFuture<IngredientResponse> updateIngredient(String ingredientId, IngredientUpdateRequest ingredientUpdateRequest) {
        return lambdaServiceClient.updateIngredient(ingredientId, ingredientUpdateRequest).thenApply(ingredientResponse -> {
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
    public CompletableFuture<DeleteIngredientResponse> deleteIngredient(String ingredientId) {
        return lambdaServiceClient.deleteIngredientById(ingredientId).thenApply(deleteResponse -> {
            if (deleteResponse != null && deleteResponse.getId() != null) {
                ingredientRepository.deleteById(deleteResponse.getId());
            }
            return deleteResponse;
        });
    }

    @Async
    @Qualifier("TaskScheduler-")
    public void checkAndReplenishStock() {
        List<IngredientRecord> ingredients = (List<IngredientRecord>) ingredientRepository.findAll();
        for (IngredientRecord ingredient : ingredients) {
            if (ingredient.getQuantity() < 100) {
                ingredient.setQuantity(ingredient.getQuantity() + 500);
                ingredientRepository.save(ingredient);
                System.out.println("Replenished stock for " + ingredient.getName());
            }
        }
    }
}
