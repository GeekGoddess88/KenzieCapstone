package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class DeleteIngredient implements RequestHandler<Map<String, String>, Map<String, Object>> {

    private final DynamoDB dynamoDB;
    private final Table ingredientTable;

    public DeleteIngredient() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(client);
        ingredientTable = dynamoDB.getTable("Ingredients");
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, String> input, Context context) {
        String ingredientId = input.get("id");
        Map<String, Object> response = new HashMap<>();

        try {
            ingredientTable.deleteItem("id", ingredientId);
            response.put("statusCode", 200);
            response.put("body", "{\"message\":\"Ingredient deleted successfully\"}");
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body", "{\"message\":\"Error deleting ingredient\", \"error\":\"" + e.getMessage() + "\"}");
        }

        return response;
    }
}
