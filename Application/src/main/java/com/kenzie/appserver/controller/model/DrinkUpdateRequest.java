package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Ingredient;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class DrinkUpdateRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;
    @JsonProperty("ingredients")
    private List<Ingredient> ingredients;
    @JsonProperty("recipe")
    private String recipe;

//    public DrinkUpdateRequest() {}
//
//    public DrinkUpdateRequest(String name, List<Ingredient> ingredients, String recipe) {
//        this.name = name;
//        this.ingredients = ingredients;
//        this.recipe = recipe;
//    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe() {
        return recipe;
    }
    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
