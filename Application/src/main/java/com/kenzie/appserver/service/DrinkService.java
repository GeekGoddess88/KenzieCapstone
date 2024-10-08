package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.model.DrinkResponse;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.DrinkUpdateRequest;
import com.kenzie.capstone.service.model.DrinkCreateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final LambdaServiceClient lambdaServiceClient;
    private final CloudWatchService cloudWatchService;
    private final TaskExecutor taskExecutor;

    @Autowired
    public DrinkService(DrinkRepository drinkRepository, CloudWatchService cloudWatchService,
        LambdaServiceClient lambdaServiceClient, TaskExecutor taskExecutor) {
        this.drinkRepository = drinkRepository;
        this.cloudWatchService = cloudWatchService;
        this.lambdaServiceClient = lambdaServiceClient;
        this.taskExecutor = taskExecutor;
    }

    @Async
    public CompletableFuture<DrinkResponse> addDrink(DrinkCreateRequest drinkCreateRequest) throws IOException {
        return lambdaServiceClient.addDrink(drinkCreateRequest).thenApply(drinkResponse -> {
            DrinkRecord drinkRecord = new DrinkRecord(
                    drinkResponse.getId(),
                    drinkResponse.getName(),
                    drinkResponse.getIngredients(),
                    drinkResponse.getRecipe()
            );
            drinkRepository.save(drinkRecord);
            cloudWatchService.publishMetric("AddDrinkLatency", System.currentTimeMillis());
            return drinkResponse;
        });
    }


    @Async
    public CompletableFuture<DrinkResponse> getDrinkById(String drinkId) {
        return drinkRepository.findById(drinkId)
                .map(record -> CompletableFuture.completedFuture(new DrinkResponse(
                        record.getId(),
                        record.getName(),
                        record.getIngredients(),
                        record.getRecipe()
                )))
                .orElseGet(() -> {
                    try {
                        return lambdaServiceClient.getDrinkById(drinkId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Async
    public CompletableFuture<List<DrinkResponse>> getAllDrinks() {
            return CompletableFuture.supplyAsync(() -> {
                    List<DrinkRecord> drinkRecords = StreamSupport
                            .stream(drinkRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
                    if (!drinkRecords.isEmpty()) {
                        return drinkRecords.stream()
                                .map(record -> new DrinkResponse(record.getId(),
                                        record.getName(),
                                        record.getIngredients(),
                                        record.getRecipe()))
                                .collect(Collectors.toList());
                    }

                    return null;
            }, taskExecutor).thenCompose(localResult -> {
                if (localResult == null) {
                    try {
                        return lambdaServiceClient.getAllDrinks()
                                .thenApply(List::of)
                                .exceptionally(ex -> {
                                    System.err.println("Error fetching drinks from Lambda: " + ex.getMessage());
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
    public CompletableFuture<DrinkResponse> updateDrink(String drinkId, DrinkUpdateRequest drinkUpdateRequest) {
        try {
            return lambdaServiceClient.updateDrink(drinkId, drinkUpdateRequest).thenApply(drinkResponse -> {
                DrinkRecord drinkRecord = new DrinkRecord(
                        drinkResponse.getId(),
                        drinkResponse.getName(),
                        drinkResponse.getIngredients(),
                        drinkResponse.getRecipe()
                );
                drinkRepository.save(drinkRecord);
                cloudWatchService.publishMetric("UpdateDrinkLatency", System.currentTimeMillis());
                return drinkResponse;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public CompletableFuture<DeleteDrinkResponse> deleteDrinkById(String drinkId) {
        try {
            return lambdaServiceClient.deleteDrinkById(drinkId).thenApply(deleteResponse -> {
                if (deleteResponse != null && deleteResponse.getId() != null) {
                    drinkRepository.deleteById(deleteResponse.getId());
                }
                cloudWatchService.publishMetric("DeleteDrinkLatency", System.currentTimeMillis());
                return deleteResponse;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
