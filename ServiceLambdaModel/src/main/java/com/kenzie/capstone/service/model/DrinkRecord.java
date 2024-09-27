package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;

import java.util.List;

@DynamoDBTable(tableName = "drinks")
public class DrinkRecord {

    private String id;
    private String name;
    private List<IngredientRecord> ingredients;
    private String recipe;

    public DrinkRecord() {}

    public DrinkRecord(String id, String name, List<IngredientRecord> ingredients, String recipe) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "ingredients")
    @DynamoDBTypeConvertedJson
    public List<IngredientRecord> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<IngredientRecord> ingredients) {
        this.ingredients = ingredients;
    }

    @DynamoDBAttribute(attributeName = "recipe")
    public String getRecipe() {
        return recipe;
    }
    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
