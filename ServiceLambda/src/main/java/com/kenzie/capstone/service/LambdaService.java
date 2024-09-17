package com.kenzie.capstone.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientRecord;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LambdaService {

    private final AWSLambda lambdaClient;
    private final ObjectMapper objectMapper;

    public LambdaService() {
        this.lambdaClient = AWSLambdaClientBuilder.defaultClient();
        this.objectMapper = new ObjectMapper();
    }

    public DrinkRecord findById(String functionName, String drinkId) throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of("id", drinkId));

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult result = lambdaClient.invoke(request);

        if (result.getStatusCode() == 200) {
            String jsonResponse = new String(result.getPayload().array(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonResponse, DrinkRecord.class);
        } else {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public List<DrinkRecord> findAllDrinks(String functionName) throws Exception {
        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload("{}");

        InvokeResult result = lambdaClient.invoke(request);

        if (result.getStatusCode() == 200) {
            String jsonResponse = new String(result.getPayload().array(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, DrinkRecord.class));
        } else {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public void saveDrink(String functionName, DrinkRecord drink) throws Exception {
        String payload = objectMapper.writeValueAsString(drink);

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        lambdaClient.invoke(request);
    }

    public void updateDrink(String functionName, DrinkRecord drink) throws Exception {
        String payload = objectMapper.writeValueAsString(drink);

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult result = lambdaClient.invoke(request);
        if (result.getStatusCode() != 200) {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public void deleteDrinkById(String functionName, String drinkId) throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of("id", drinkId));

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);
        lambdaClient.invoke(request);
    }

    public IngredientRecord findIngredientById(String functionName, String ingredientId) throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of("id", ingredientId));

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult result = lambdaClient.invoke(request);

        if (result.getStatusCode() == 200) {
            String jsonResponse = new String(result.getPayload().array(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonResponse, IngredientRecord.class);
        } else {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public List<IngredientRecord> findAllIngredients(String functionName) throws Exception {
        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload("{}");

        InvokeResult result = lambdaClient.invoke(request);

        if (result.getStatusCode() == 200) {
            String jsonResponse = new String(result.getPayload().array(), StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, IngredientRecord.class));
        } else {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public void saveIngredient(String functionName, IngredientRecord ingredientRecord) throws Exception {
        String payload = objectMapper.writeValueAsString(ingredientRecord);

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult result = lambdaClient.invoke(request);

        if (result.getStatusCode() != 200) {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public void updateIngredient(String functionName, IngredientRecord ingredientRecord) throws Exception {
        String payload = objectMapper.writeValueAsString(ingredientRecord);

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult result = lambdaClient.invoke(request);
        if (result.getStatusCode() != 200) {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }

    public void deleteIngredientById(String functionName, String ingredientId) throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of("id", ingredientId));

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload);

        InvokeResult result = lambdaClient.invoke(request);

        if (result.getStatusCode() != 200) {
            throw new Exception("Failed to invoke function " + result.getFunctionError());
        }
    }
}
