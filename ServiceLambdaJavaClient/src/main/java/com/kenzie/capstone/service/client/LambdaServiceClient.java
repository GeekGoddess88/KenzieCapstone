package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.kenzie.capstone.service.model.*;


import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class LambdaServiceClient {

    private static final String ADD_DRINK_ENDPOINT = "/drinks";
    private static final String GET_DRINK_ENDPOINT = "/drinks/{id}";
    private static final String UPDATE_DRINK_ENDPOINT = "/drinks/{id}";
    private static final String DELETE_DRINK_ENDPOINT = "/drinks/{id}";
    private static final String GET_ALL_DRINKS_ENDPOINT = "/drinks/all";

    private static final String ADD_INGREDIENT_ENDPOINT = "/ingredients";
    private static final String GET_INGREDIENT_ENDPOINT = "/ingredients/{id}";
    private static final String UPDATE_INGREDIENT_ENDPOINT = "/ingredients/{id}";
    private static final String DELETE_INGREDIENT_ENDPOINT = "/ingredients/{id}";
    private static final String GET_ALL_INGREDIENTS_ENDPOINT = "/ingredients/all";
    private final EndpointUtility endpointUtility;
    private final ObjectMapper objectMapper;
    public Executor taskExecutor;


    @Inject
    public LambdaServiceClient(EndpointUtility endpointUtility, @Named("LambdaServiceClient-Executor") Executor taskExecutor) {
        this.endpointUtility = endpointUtility;
        this.objectMapper = new ObjectMapper();
        this.taskExecutor = taskExecutor;
    }

    private String buildUrlWithId(String endpoint, String id) {
        return endpoint.replace("{id}", id);
    }

    public CompletableFuture<DrinkResponse> addDrink(DrinkCreateRequest drinkCreateRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String requestBody = objectMapper.writeValueAsString(drinkCreateRequest);
                String responseJson = endpointUtility.postEndpoint(ADD_DRINK_ENDPOINT, requestBody);
                return objectMapper.readValue(responseJson, DrinkResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to add drink via Lambda",e);
            }
                }, taskExecutor);
    }

    public CompletableFuture<DrinkResponse> getDrinkById(String drinkId) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String endpoint = buildUrlWithId(GET_DRINK_ENDPOINT, drinkId);
                String response = endpointUtility.getEndpoint(endpoint);
                return objectMapper.readValue(response, DrinkResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get drink via Lambda",e);
            }
                }, taskExecutor);
    }

    public CompletableFuture<DrinkResponse[]> getAllDrinks() throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String response = endpointUtility.getEndpoint(GET_ALL_DRINKS_ENDPOINT);
                return objectMapper.readValue(response, DrinkResponse[].class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get drinks via Lambda",e);
            }
                }, taskExecutor);
    }

    public CompletableFuture<DrinkResponse> updateDrink(String id, DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String endpoint = buildUrlWithId(UPDATE_DRINK_ENDPOINT, id);
                String requestBody = objectMapper.writeValueAsString(drinkUpdateRequest);
                String response = endpointUtility.putEndpoint(endpoint, requestBody);
                return objectMapper.readValue(response, DrinkResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update drink via Lambda",e);
            }
        }, taskExecutor);
    }

    public CompletableFuture<DeleteDrinkResponse> deleteDrinkById(String drinkId) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String endpoint = buildUrlWithId(DELETE_DRINK_ENDPOINT, drinkId);
                String response = endpointUtility.deleteEndpoint(endpoint);
                return objectMapper.readValue(response, DeleteDrinkResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete drink via Lambda",e);
            }
        }, taskExecutor);
    }

    public CompletableFuture<IngredientResponse> addIngredient(IngredientCreateRequest ingredientCreateRequest) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String requestBody = objectMapper.writeValueAsString(ingredientCreateRequest);
                String response = endpointUtility.postEndpoint(ADD_INGREDIENT_ENDPOINT, requestBody);
                return objectMapper.readValue(response, IngredientResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to add ingredient via Lambda",e);
            }
        }, taskExecutor);
    }

    public CompletableFuture<IngredientResponse> getIngredientById(String ingredientId) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String endpoint = buildUrlWithId(GET_INGREDIENT_ENDPOINT, ingredientId);
                String response = endpointUtility.getEndpoint(endpoint);
                return objectMapper.readValue(response, IngredientResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get drink via Lambda",e);
            }
        }, taskExecutor);
    }

    public CompletableFuture<IngredientResponse[]> getAllIngredients() throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String response = endpointUtility.getEndpoint(GET_ALL_INGREDIENTS_ENDPOINT);
                return objectMapper.readValue(response, IngredientResponse[].class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get drinks via Lambda",e);
            }
        }, taskExecutor);
    }

    public CompletableFuture<IngredientResponse> updateIngredient(String id, IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String endpoint = buildUrlWithId(UPDATE_INGREDIENT_ENDPOINT, id);
                String requestBody = objectMapper.writeValueAsString(ingredientUpdateRequest);
                String response = endpointUtility.putEndpoint(endpoint, requestBody);
                return objectMapper.readValue(response, IngredientResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update ingredient via Lambda",e);
            }
        }, taskExecutor);
    }

    public CompletableFuture<DeleteIngredientResponse> deleteIngredientById(String id) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String endpoint = buildUrlWithId(DELETE_INGREDIENT_ENDPOINT, id);
                String response = endpointUtility.deleteEndpoint(endpoint);
                return objectMapper.readValue(response, DeleteIngredientResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete ingredient via Lambda",e);
            }
        }, taskExecutor);
    }
}
