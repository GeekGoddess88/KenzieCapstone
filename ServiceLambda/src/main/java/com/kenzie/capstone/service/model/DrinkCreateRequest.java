package com.kenzie.capstone.service.model;

import com.kenzie.capstone.service.dao.IngredientDAO;


import java.util.List;


public class DrinkCreateRequest {
    private String name;
    private String recipe;
    private List<IngredientInterface> ingredients;

    public DrinkCreateRequest() {}

    public DrinkCreateRequest(String name, String recipe, List<IngredientInterface> ingredients) {
        this.name = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public DrinkRecord toDrinkRecord(String id) {
        return new DrinkRecord(id, name, ingredients, recipe);
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

    public List<IngredientInterface> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientInterface> ingredients) {
        this.ingredients = ingredients;
    }
}
