package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.IngredientDAO;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class IngredientService {

    private final LambdaServiceClient lambdaServiceClient;

    @Inject
    public IngredientService(LambdaServiceClient lambdaServiceClient) {
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) {
        try {
            return lambdaServiceClient.addIngredient(ingredientCreateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public IngredientResponse getIngredientById(String id) {
        try {
            return lambdaServiceClient.getIngredientById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<IngredientResponse> getAllIngredients() {
        try {
            return lambdaServiceClient.getAllIngredients();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public IngredientResponse updateIngredient(String ingredientId, IngredientUpdateRequest ingredientUpdateRequest) {
        try {
            return lambdaServiceClient.updateIngredient(ingredientId, ingredientUpdateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DeleteIngredientResponse deleteIngredient(String ingredientId) {
        try {
            return lambdaServiceClient.deleteIngredientById(ingredientId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
