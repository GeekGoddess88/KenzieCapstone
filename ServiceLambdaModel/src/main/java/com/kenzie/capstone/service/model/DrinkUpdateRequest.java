package com.kenzie.capstone.service.model;

import java.util.List;

public class DrinkUpdateRequest {

    private String id;
    private String name;
    private String recipe;
    private List<IngredientRecord> ingredients;

    public DrinkUpdateRequest() {}

    public DrinkUpdateRequest(String id, String name, String recipe, List<IngredientRecord> ingredients) {
        this.id = id;
        this.name = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public List<IngredientRecord> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRecord> ingredients) {
        this.ingredients = ingredients;
    }
}
