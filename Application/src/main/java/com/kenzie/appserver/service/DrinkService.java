package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.model.converter.DrinkConverter;
import com.kenzie.capstone.service.model.converter.IngredientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Named;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DrinkService {

    @Qualifier("drinkRepository")
    private final DrinkRepository drinkRepository;
    private final LambdaServiceClient lambdaServiceClient;
    @Qualifier("taskExecutor")
    private final TaskExecutor taskExecutor;
    private final DrinkConverter drinkConverter;
    private final IngredientConverter ingredientConverter;

    @Autowired
    public DrinkService(DrinkRepository drinkRepository,
        LambdaServiceClient lambdaServiceClient, @Qualifier("taskExecutor")TaskExecutor taskExecutor) {
        this.drinkRepository = drinkRepository;
        this.lambdaServiceClient = lambdaServiceClient;
        this.taskExecutor = taskExecutor;
        this.ingredientConverter = new IngredientConverter();
        this.drinkConverter = new DrinkConverter(ingredientConverter);
    }

    @Async
    public CompletableFuture<DrinkResponse> addDrink(DrinkCreateRequest drinkCreateRequest) throws IOException {
        return lambdaServiceClient.addDrink(drinkCreateRequest).thenApply(drinkResponse -> {
            DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkCreateRequest);
            drinkRepository.save(drinkRecord);
            return drinkResponse;
        });
    }


    @Async
    public CompletableFuture<DrinkResponse> getDrinkById(String drinkId) {
        return drinkRepository.findById(drinkId)
                .map(record -> CompletableFuture.completedFuture(drinkConverter.toDrinkResponse(record)))
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
                                .map(drinkConverter::toDrinkResponse)
                                .collect(Collectors.toList());
                    }

                    return null;
            }, taskExecutor).thenCompose(localResult -> {
                if (localResult == null) {
                    try {
                        return lambdaServiceClient.getAllDrinks()
                                .thenApply(Arrays::asList)
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
                DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkUpdateRequest);
                drinkRepository.save(drinkRecord);
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
                return deleteResponse;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
