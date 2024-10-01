package com.kenzie.appserver.service.model;


import java.util.List;

public class Drink {
    private final String id;
    private final String name;
    private final List<Ingredient> ingredients;
    private final String recipe;

    public Drink(String id, String name, List<Ingredient> ingredients, String recipe) {
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
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public String getRecipe() {
        return recipe;
    }
}
