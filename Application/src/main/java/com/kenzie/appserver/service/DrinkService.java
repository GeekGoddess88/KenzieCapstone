package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.DrinkRepository;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.model.DrinkResponse;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.DrinkUpdateRequest;
import com.kenzie.capstone.service.model.DrinkCreateRequest;

import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final LambdaServiceClient lambdaServiceClient;

    @Inject
    public DrinkService(DrinkRepository drinkRepository, LambdaServiceClient lambdaServiceClient) {
        this.drinkRepository = drinkRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) throws IOException {
        DrinkResponse drinkResponse = lambdaServiceClient.addDrink(drinkCreateRequest);
        DrinkRecord drinkRecord = new DrinkRecord(
                drinkResponse.getId(),
                drinkResponse.getName(),
                drinkResponse.getIngredients(),
                drinkResponse.getRecipe());
        drinkRepository.save(drinkRecord);
        return drinkResponse;
    }

    public DrinkResponse getDrinkById(String drinkId) throws IOException {
        return drinkRepository.findById(drinkId)
                .map(record -> new DrinkResponse(record.getId(), record.getName(), record.getIngredients(), record.getRecipe()))
                .orElseGet(() -> {
                    try {
                        return lambdaServiceClient.getDrinkById(drinkId);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get drink from Lambda", e);
                    }
                });
    }

    public List<DrinkResponse> getAllDrinks() throws IOException {
        List<DrinkRecord> drinkRecords = StreamSupport.stream(drinkRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        if (drinkRecords.isEmpty()) {
            DrinkResponse[] drinkResponses = lambdaServiceClient.getAllDrinks();
            return Arrays.asList(drinkResponses);
        }
        return drinkRecords.stream()
                .map(record -> new DrinkResponse(record.getId(), record.getName(), record.getIngredients(), record.getRecipe()))
                .collect(Collectors.toList());
    }

    public DrinkResponse updateDrink(String drinkId, DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        DrinkResponse drinkResponse = lambdaServiceClient.updateDrink(drinkId, drinkUpdateRequest);
        DrinkRecord drinkRecord = new DrinkRecord(drinkResponse.getId(), drinkResponse.getName(), drinkResponse.getIngredients(), drinkResponse.getRecipe());
        drinkRepository.save(drinkRecord);
        return drinkResponse;
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) throws IOException {
        DeleteDrinkResponse deleteResponse = lambdaServiceClient.deleteDrinkById(drinkId);
        if (deleteResponse != null && deleteResponse.getId() != null) {
            drinkRepository.deleteById(deleteResponse.getId());
        }
        return deleteResponse;
    }
}
