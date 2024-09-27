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

public class AddIngredientById implements RequestHandler<Map<String, Object>, Map<String, Object>>  {

    private final DynamoDB dynamoDB;
    private final Table ingredientTable;

    public AddIngredientById() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(client);
        ingredientTable = dynamoDB.getTable("Ingredients");
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            IngredientRecord ingredient = new ObjectMapper().convertValue(input.get("body"), IngredientRecord.class);

            ingredientTable.putItem(new com.amazonaws.services.dynamodbv2.document.Item()
                    .withPrimaryKey("id", ingredient.getId())
                    .withString("name", ingredient.getName())
                    .withNumber("quantity", ingredient.getQuantity()));

            response.put("statusCode", 200);
            response.put("body", "{\"message\":\"Ingredient saved successfully\"}");
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body", "{\"message\":\"Error saving ingredient\", \"error\":\"" + e.getMessage() + "\"}");
        }

        return response;
    }
}
