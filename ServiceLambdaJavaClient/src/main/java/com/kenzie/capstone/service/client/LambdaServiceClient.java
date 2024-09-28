package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kenzie.capstone.service.model.*;
import dagger.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class LambdaServiceClient {

    private static final String ADD_DRINK_ENDPOINT = "/drinks";
    private static final String GET_DRINK_ENDPOINT = "/drinks/{id}";
    private static final String UPDATE_DRINK_ENDPOINT = "/drinks/{id}";
    private static final String DELETE_DRINK_ENDPOINT = "/drinks/{id}";
    private static final String GET_ALL_DRINKS_ENDPOINT = "/drinks";

    private static final String ADD_INGREDIENT_ENDPOINT = "/ingredients";
    private static final String GET_INGREDIENT_ENDPOINT = "/ingredients/{id}";
    private static final String UPDATE_INGREDIENT_ENDPOINT = "/ingredients/{id}";
    private static final String DELETE_INGREDIENT_ENDPOINT = "/ingredients/{id}";
    private static final String GET_ALL_INGREDIENTS_ENDPOINT = "/ingredients";
    private final EndpointUtility endpointUtility;
    private final ObjectMapper objectMapper;


    @Inject
    public LambdaServiceClient(EndpointUtility endpointUtility) {
        this.endpointUtility = endpointUtility;
        this.objectMapper = new ObjectMapper();
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) throws IOException {
        String requestBody = objectMapper.writeValueAsString(drinkCreateRequest);
        String responseJson = endpointUtility.postEndpoint(ADD_DRINK_ENDPOINT, requestBody);
        return objectMapper.readValue(responseJson, DrinkResponse.class);
    }

    public DrinkResponse getDrinkById(String drinkId) throws IOException {
        String endpoint = GET_DRINK_ENDPOINT.replace("{id}", drinkId);
        String response = endpointUtility.getEndpoint(endpoint);
        return objectMapper.readValue(response, DrinkResponse.class);
    }

    public DrinkResponse[] getAllDrinks() throws IOException {
        String response = endpointUtility.getEndpoint(GET_ALL_DRINKS_ENDPOINT);
        return objectMapper.readValue(response, DrinkResponse[].class);
    }

    public DrinkResponse updateDrink(String id, DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        String endpoint = UPDATE_DRINK_ENDPOINT.replace("{id}", id);
        String requestBody = objectMapper.writeValueAsString(drinkUpdateRequest);
        String response = endpointUtility.putEndpoint(endpoint, requestBody);
        return objectMapper.readValue(response, DrinkResponse.class);
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) throws IOException {
        String endpoint = DELETE_DRINK_ENDPOINT.replace("{id}", drinkId);
        String response = endpointUtility.deleteEndpoint(endpoint);
        return objectMapper.readValue(response, DeleteDrinkResponse.class);
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) throws IOException {
        String requestBody = objectMapper.writeValueAsString(ingredientCreateRequest);
        String response = endpointUtility.postEndpoint(ADD_INGREDIENT_ENDPOINT, requestBody);
        return objectMapper.readValue(response, IngredientResponse.class);
    }

    public IngredientResponse getIngredientById(String ingredientId) throws IOException {
        String endpoint = GET_INGREDIENT_ENDPOINT.replace("{id}", ingredientId);
        String response = endpointUtility.getEndpoint(endpoint);
        return objectMapper.readValue(response, IngredientResponse.class);
    }

    public IngredientResponse[] getAllIngredients() throws IOException {
        String response = endpointUtility.getEndpoint(GET_ALL_INGREDIENTS_ENDPOINT);
        return objectMapper.readValue(response, IngredientResponse[].class);
    }

    public IngredientResponse updateIngredient(String id, IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
        String endpoint = UPDATE_INGREDIENT_ENDPOINT.replace("{id}", id);
        String requestBody = objectMapper.writeValueAsString(ingredientUpdateRequest);
        String response = endpointUtility.putEndpoint(endpoint, requestBody);
        return objectMapper.readValue(response, IngredientResponse.class);
    }

    public DeleteIngredientResponse deleteIngredientById(String id) throws IOException {
        String endpoint = DELETE_INGREDIENT_ENDPOINT.replace("{id}", id);
        String response = endpointUtility.deleteEndpoint(endpoint);
        return objectMapper.readValue(response, DeleteIngredientResponse.class);
    }
}
