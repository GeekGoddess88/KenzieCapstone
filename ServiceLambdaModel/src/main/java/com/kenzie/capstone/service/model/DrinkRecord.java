package com.kenzie.capstone.service.model;

import com.kenzie.capstone.service.model.IngredientInterface;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "Drinks")
public class DrinkRecord implements DrinkInterface {
    private String id;
    private String name;
    private List<IngredientInterface> ingredients;
    private String recipe;

    public DrinkRecord() {}

    public DrinkRecord(String id, String name, List<IngredientInterface> ingredients, String recipe) {}

    @Override
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @DynamoDBAttribute(attributeName = "ingredients")
    public List<IngredientInterface> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientInterface> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    @DynamoDBAttribute(attributeName = "recipe")
    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
