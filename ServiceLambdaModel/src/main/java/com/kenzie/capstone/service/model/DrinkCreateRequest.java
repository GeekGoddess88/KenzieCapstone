package com.kenzie.capstone.service.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


public class DrinkCreateRequest {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("recipe")
    private String recipe;
    @JsonProperty("ingredients")
    private List<IngredientCreateRequest> ingredients;


    public DrinkCreateRequest(String id, String name, String recipe, List<IngredientCreateRequest> ingredients) {
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

    public List<IngredientCreateRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientCreateRequest> ingredients) {
        this.ingredients = ingredients;
    }


}
