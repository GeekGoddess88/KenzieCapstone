package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class GetIngredientById implements RequestHandler<Map<String, String>, Map<String, Object>> {

    private final DynamoDB dynamoDB;
    private final Table ingredientTable;

    public GetIngredientById() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(client);
        ingredientTable = dynamoDB.getTable("Ingredients");
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, String> input, Context context) {
        String ingredientId = input.get("id");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> item = ingredientTable.getItem("id", ingredientId).asMap();
            if (item != null) {
                response.put("statusCode", 200);
                response.put("body", new ObjectMapper().writeValueAsString(item));
            } else {
                response.put("statusCode", 404);
                response.put("body", "{\"message\": \"Ingredient not found\"}");
            }
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body","{\"message\":\"Error retrieving ingredient\", \"error\": \"" + e.getMessage() + "\"}");
        }

        return response;
    }
}
