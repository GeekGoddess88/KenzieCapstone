package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kenzie.appserver.repositories.model.IngredientRepository;
import com.kenzie.capstone.service.client.LambdaServiceClient;

import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final LambdaServiceClient lambdaServiceClient;


    @Inject
    public IngredientService(IngredientRepository ingredientRepository, LambdaServiceClient lambdaServiceClient) {
        this.ingredientRepository = ingredientRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) throws IOException {
        IngredientResponse ingredientResponse = lambdaServiceClient.addIngredient(ingredientCreateRequest);
        IngredientRecord ingredientRecord = new IngredientRecord(
                ingredientResponse.getId(),
                ingredientResponse.getName(),
                ingredientResponse.getQuantity());
        ingredientRepository.save(ingredientRecord);
        return ingredientResponse;
    }

    public IngredientResponse getIngredientById(String id) throws IOException {
        return ingredientRepository.findById(id)
                .map(record -> new IngredientResponse(record.getId(), record.getName(), record.getQuantity()))
                .orElseGet(() -> {
                    try {
                        return lambdaServiceClient.getIngredientById(id);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get Ingredient from the Lambda", e);
                    }
                });
    }

    public List<IngredientResponse> getAllIngredients() throws IOException {
        List<IngredientRecord> ingredientRecords = StreamSupport.stream(ingredientRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        if (ingredientRecords.isEmpty()) {
            IngredientResponse[] ingredientResponses = lambdaServiceClient.getAllIngredients();
            return Arrays.asList(ingredientResponses);
        }
        return ingredientRecords.stream()
                .map(record -> new IngredientResponse(record.getId(), record.getName(), record.getQuantity()))
                .collect(Collectors.toList());
    }

    public IngredientResponse updateIngredient(String ingredientId, IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
            IngredientResponse ingredientResponse = lambdaServiceClient.updateIngredient(ingredientId, ingredientUpdateRequest);
            IngredientRecord ingredientRecord = new IngredientRecord(ingredientResponse.getId(), ingredientResponse.getName(), ingredientResponse.getQuantity());
            ingredientRepository.save(ingredientRecord);
            return ingredientResponse;
    }

    public DeleteIngredientResponse deleteIngredient(String ingredientId) throws IOException {
            DeleteIngredientResponse deleteIngredientResponse = lambdaServiceClient.deleteIngredientById(ingredientId);
            if (deleteIngredientResponse != null && deleteIngredientResponse.getId() != null) {
                ingredientRepository.deleteById(deleteIngredientResponse.getId());
            }
            return deleteIngredientResponse;
    }
}
