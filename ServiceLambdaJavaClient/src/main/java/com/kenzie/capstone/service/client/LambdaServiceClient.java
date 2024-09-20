package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;


public class LambdaServiceClient {

    private final EndpointUtility endpointUtility;
    private final ObjectMapper objectMapper;


    @Inject
    public LambdaServiceClient(EndpointUtility endpointUtility, ObjectMapper objectMapper) {
        this.endpointUtility = endpointUtility;
        this.objectMapper = objectMapper;
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(drinkCreateRequest);
        String responseJson = endpointUtility.postEndpoint("/drinks", requestJson);
        return objectMapper.readValue(responseJson, DrinkResponse.class);
    }

    public DrinkResponse getDrinkById(String drinkId) throws Exception {
        String responseJson = endpointUtility.getEndpoint("/drinks/" + drinkId);
        return objectMapper.readValue(responseJson, DrinkResponse.class);
    }

    public List<DrinkResponse> getAllDrinks() throws Exception {
        String responseJson = endpointUtility.getEndpoint("/drinks");
        return Arrays.asList(objectMapper.readValue(responseJson, DrinkResponse[].class));
    }

    public DrinkResponse updateDrink(String id, DrinkUpdateRequest drinkUpdateRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(drinkUpdateRequest);
        String responseJson = endpointUtility.putEndpoint("/drinks/" + id, requestJson);
        return objectMapper.readValue(responseJson, DrinkResponse.class);
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) throws Exception {
        endpointUtility.deleteEndpoint("/drinks/" + drinkId);
        return new DeleteDrinkResponse(drinkId, "Drink deleted successfully");
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(ingredientCreateRequest);
        String responseJson = endpointUtility.postEndpoint("/ingredients", requestJson);
        return objectMapper.readValue(responseJson, IngredientResponse.class);
    }

    public IngredientResponse getIngredientById(String ingredientId) throws Exception {
        String responseJson = endpointUtility.getEndpoint("/ingredients/" + ingredientId);
        return objectMapper.readValue(responseJson, IngredientResponse.class);
    }

    public List<IngredientResponse> getAllIngredients() throws Exception {
        String responseJson = endpointUtility.getEndpoint("/ingredients");
        return Arrays.asList(objectMapper.readValue(responseJson, IngredientResponse[].class));
    }

    public IngredientResponse updateIngredient(String id, IngredientUpdateRequest ingredientUpdateRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(ingredientUpdateRequest);
        String responseJson = endpointUtility.putEndpoint("/ingredients/" + id, requestJson);
        return objectMapper.readValue(responseJson, IngredientResponse.class);
    }

    public DeleteIngredientResponse deleteIngredientById(String id) throws Exception {
        endpointUtility.deleteEndpoint("/ingredients/" + id);
        return new DeleteIngredientResponse(id, "Ingredient deleted successfully");
    }
}
