package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientInterface;
import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.*;
/* Still fixing
just have a few methods to redo
 */


public class DrinkNonCachingDAO implements DrinkDAO {

    private final AmazonDynamoDB amazonDynamoDB;
    private final Table drinkTable;

    public DrinkNonCachingDAO(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.drinkTable = new DynamoDB(amazonDynamoDB).getTable("Drinks");
    }

    @Override
    public DrinkRecord findById(String id) {
        GetItemRequest request = new GetItemRequest()
                .withTableName("Drinks")
                .withKey(Map.of("id", new AttributeValue(id)));

        GetItemResult result = amazonDynamoDB.getItem(request);

        if (result.getItem() == null) {
            return null;
        }

        Map<String, AttributeValue> item = result.getItem();
        DrinkRecord drink = new DrinkRecord();
        drink.setId(item.get("id").getS());
        drink.setName(item.get("name").getS());
        drink.setIngredients(parseIngredients(item.get("ingredients").getL()));
        drink.setRecipe(item.get("recipe").getS());

        return drink;
    }

    @Override
    public void save(DrinkRecord drinkRecord) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", new AttributeValue(drinkRecord.getId()));
        item.put("name", new AttributeValue(drinkRecord.getName()));
        item.put("ingredients", new AttributeValue().withL(serializeIngredients(drinkRecord.getIngredients())));
        item.put("recipe", new AttributeValue(drinkRecord.getRecipe()));

        PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName("Drinks")
                .withItem(item);

        amazonDynamoDB.putItem(putItemRequest);
    }

    @Override
    public void delete(String id) {
        DrinkRecord drinkRecord = findById(id);
        if (drinkRecord != null) {
            dynamoDBMapper.delete(drinkRecord);
        }
    }

    @Override
    public List<DrinkRecord> findAll() {
        List<DrinkRecord> drinks = new ArrayList<>();

        ScanRequest scanRequest = new ScanRequest().withTableName("Drinks");

        ScanResult result = getAmazonDynamoDB.scan(scanRequest);
    }

    private List<IngredientInterface> parseIngredients(List<AttributeValue> ingredientAttributes) {
        List<IngredientInterface> ingredients = new ArrayList<>();
        for (AttributeValue attr : ingredientAttributes) {
            IngredientRecord ingredient = new IngredientRecord();
            ingredient.setName(attr.getM().get("name").getS());
            ingredient.setQuantity(Integer.parseInt(attr.getM().get("quantity").getN()));
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    private List<AttributeValue> serializeIngredients(List<IngredientInterface> ingredients) {
        List<AttributeValue> ingredientAttributes = new ArrayList<>();
        for (IngredientInterface ingredient : ingredients) {
            Map<String, AttributeValue> ingredientMap = new HashMap<>();
            ingredientMap.put("name", new AttributeValue(ingredient.getName()));
            ingredientMap.put("quantity", new AttributeValue(String.valueOf(ingredient.getQuantity())));
            ingredientAttributes.add(new AttributeValue().withM(ingredientMap));
        }
        return ingredientAttributes;
    }
}
