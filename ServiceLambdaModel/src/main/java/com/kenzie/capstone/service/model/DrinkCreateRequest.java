package com.kenzie.capstone.service.model;


import java.util.List;


public class DrinkCreateRequest {
    private String id;
    private String name;
    private String recipe;
    private List<IngredientRecord> ingredients;

    public DrinkCreateRequest() {}

    public DrinkCreateRequest(String id, String name, String recipe, List<IngredientRecord> ingredients) {
        this.id = id;
        this.name = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRecipe() {
        return recipe;
    }

    public List<IngredientRecord> getIngredients() {
        return ingredients;
    }


}
