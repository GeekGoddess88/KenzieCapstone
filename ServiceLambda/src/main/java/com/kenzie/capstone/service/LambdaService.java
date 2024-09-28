package com.kenzie.capstone.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.kenzie.capstone.service.dao.*;
import com.kenzie.capstone.service.model.*;

import dagger.Component;


import javax.inject.Inject;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class LambdaService {

    private final DrinkService drinkService;
    private final IngredientService ingredientService;
    private final LambdaServiceClient lambdaServiceClient;

    @Inject
    public LambdaService(DrinkService drinkService, IngredientService ingredientService, LambdaServiceClient lambdaServiceClient) {
        this.drinkService = drinkService;
        this.ingredientService = ingredientService;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) throws IOException {
        DrinkResponse drinkResponse = drinkService.addDrink(drinkCreateRequest);
        return lambdaServiceClient.addDrink(drinkCreateRequest);
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) throws IOException {
        IngredientResponse ingredientResponse = ingredientService.addIngredient(ingredientCreateRequest);
        return lambdaServiceClient.addIngredient(ingredientCreateRequest);
    }

    public DrinkResponse getDrinkById(String drinkId) throws IOException {
        Optional<DrinkRecord> drinkRecordOptional = drinkService.getDrinkById(drinkId);
        if (drinkRecordOptional.isPresent()) {
            return drinkService.getDrinkById(drinkId);
        }
        return lambdaServiceClient.getDrinkById(drinkId);
    }

    public IngredientResponse getIngredientById(String ingredientId) throws IOException {
        Optional<IngredientRecord> ingredientRecordOptional = ingredientService.getIngredientById(ingredientId);
        if (ingredientRecordOptional.isPresent()) {
            return ingredientService.getIngredientById(ingredientId);
        }
        return lambdaServiceClient.getIngredientById(ingredientId);
    }

    public List<DrinkResponse> getAllDrinks() throws IOException {
        List<DrinkRecord> drinks = drinkService.getAllDrinks();
        if (!drinks.isEmpty()) {
            return drinkService.getAllDrinks();
        }
         return lambdaServiceClient.getAllDrinks();
    }

    public List<IngredientResponse> getAllIngredients() throws IOException {
        List<IngredientRecord> ingredients = ingredientService.getAllIngredients();
        if (!ingredients.isEmpty()) {
            return ingredientService.getAllIngredients();
        }
        return lambdaServiceClient.getAllIngredients();
    }

    public DrinkResponse updateDrink(String id, DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        drinkService.updateDrink(id, drinkUpdateRequest);
        return lambdaServiceClient.updateDrink(id,drinkUpdateRequest);
    }

    public IngredientResponse updateIngredient(String id, IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
        ingredientService.updateIngredient(id, ingredientUpdateRequest);
        return lambdaServiceClient.updateIngredient(id, ingredientUpdateRequest);
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) throws IOException {
        drinkService.deleteDrinkById(drinkId);
        return lambdaServiceClient.deleteDrinkById(drinkId);
    }

    public DeleteIngredientResponse deleteIngredient(String ingredientId) throws IOException {
        ingredientService.deleteIngredient(ingredientId);
        return lambdaServiceClient.deleteIngredientById(ingredientId);
    }
}
