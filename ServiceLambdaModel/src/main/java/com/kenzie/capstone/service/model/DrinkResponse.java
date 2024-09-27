package com.kenzie.capstone.service.model;

import java.util.List;

public class DrinkResponse {
    private final String id;
    private final String name;
    private final String recipe;
    private List<IngredientRecord> ingredients;

    public DrinkResponse(String id, String name, List<IngredientRecord> ingredients, String recipe) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public List<IngredientRecord> getIngredients() {
        return ingredients;
    }

    public String getRecipe() {
        return recipe;
    }
}
