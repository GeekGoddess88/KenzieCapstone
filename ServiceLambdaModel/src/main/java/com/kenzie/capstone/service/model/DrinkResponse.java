package com.kenzie.capstone.service.model;

import java.util.List;

public class DrinkResponse {
    private String id;
    private String name;
    private String recipe;
    private List<IngredientInterface> ingredients;

    public DrinkResponse(DrinkRecord drinkRecord) {
        this.id = drinkRecord.getId();
        this.name = drinkRecord.getName();
        this.recipe = drinkRecord.getRecipe();
        this.ingredients = drinkRecord.getIngredients();
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

    public List<IngredientInterface> getIngredients() {
        return ingredients;
    }
}
