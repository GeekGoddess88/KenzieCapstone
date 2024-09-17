package com.kenzie.appserver.service.model;

import com.kenzie.capstone.service.model.DrinkInterface;
import com.kenzie.capstone.service.model.IngredientInterface;

import java.util.List;
import java.util.Objects;

public class Drink implements DrinkInterface {
    private String id;
    private String name;
    private String recipe;
    private List<IngredientInterface> ingredients;

    public Drink() {}

    public Drink(String id, String name, String recipe, List<IngredientInterface> ingredients) {
        this.id = id;
        this.name = name;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRecipe() {
        return recipe;
    }

    @Override
    public List<IngredientInterface> getIngredients() {
        return ingredients;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setIngredients(List<IngredientInterface> ingredients) {
        this.ingredients = ingredients;
    }

}
