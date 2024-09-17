package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.HashMap;
import java.util.Map;

public class SaveDrink implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final DynamoDB dynamoDB;
    private final Table drinksTable;

    public SaveDrink() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(client);
        drinksTable = dynamoDB.getTable("Drinks");
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            DrinkRecord drink = new ObjectMapper().convertValue(input.get("body"), DrinkRecord.class);

            drinksTable.putItem(new com.amazonaws.services.dynamodbv2.document.Item()
            .withPrimaryKey("id", drink.getId())
                    .withString("name", drink.getName())
                    .withString("recipe", drink.getRecipe())
                    .withList("ingredients", drink.getIngredients()));
            response.put("statusCode", 200);
            response.put("body", "{\"message\":\"Drink saved successfully\"}");
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body", "{\"message\":\"Error saving drink\", \"error\":\"" + e.getMessage() + "\"}");
        }

        return response;
    }
}
