package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kenzie.capstone.service.client.LambdaServiceClient;

import com.kenzie.capstone.service.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientCachingDAO ingredientCachingDAO;
    private final LambdaServiceClient lambdaServiceClient;
    private final IngredientConverter ingredientConverter;

    @Inject
    public IngredientService(IngredientCachingDAO ingredientCachingDAO, LambdaServiceClient lambdaServiceClient, IngredientConverter ingredientConverter) {
        this.ingredientCachingDAO = ingredientCachingDAO;
        this.lambdaServiceClient = lambdaServiceClient;
        this.ingredientConverter = ingredientConverter;
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) throws IOException {
        IngredientResponse ingredientResponse = lambdaServiceClient.addIngredient(ingredientCreateRequest);
        IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientResponse);
        ingredientCachingDAO.save(ingredientRecord);
        return ingredientConverter.toIngredientResponse(ingredientRecord);
    }

    public IngredientResponse getIngredientById(String id) throws IOException {
        Optional<IngredientRecord> ingredientRecordOptional = ingredientCachingDAO.findById(id);
        if (ingredientRecordOptional.isPresent()) {
            return ingredientConverter.toIngredientResponse(ingredientRecordOptional.get());
        }

        IngredientResponse ingredientResponse = lambdaServiceClient.getIngredientById(id);
        IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientResponse);
        ingredientCachingDAO.save(ingredientRecord);
        return ingredientResponse;
    }

    public List<IngredientResponse> getAllIngredients() throws IOException {
        IngredientResponse[] ingredientResponses = lambdaServiceClient.getAllIngredients();

        List<IngredientRecord> ingredientRecords = Arrays.stream(ingredientResponses)
                .map(ingredientConverter::toIngredientRecord)
                .collect(Collectors.toList());
        ingredientRecords.forEach(ingredientCachingDAO::save);
        return Arrays.asList(ingredientResponses);
    }

    public IngredientResponse updateIngredient(String ingredientId, IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
            IngredientResponse ingredientResponse = lambdaServiceClient.updateIngredient(ingredientId, ingredientUpdateRequest);
            IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientResponse);
            ingredientCachingDAO.update(ingredientId, ingredientRecord);
            return ingredientResponse;
    }

    public DeleteIngredientResponse deleteIngredient(String ingredientId) throws IOException {
            DeleteIngredientResponse deleteIngredientResponse = lambdaServiceClient.deleteIngredientById(ingredientId);
            ingredientCachingDAO.delete(ingredientId);
            return deleteIngredientResponse;
    }
}
