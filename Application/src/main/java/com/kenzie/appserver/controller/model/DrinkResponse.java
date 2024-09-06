package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Ingredient;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrinkResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("ingredients")
    private List<Ingredient> ingredients;
    @JsonProperty("recipe")
    private String recipe;

//    public DrinkResponse() {}
//
//    public DrinkResponse(String id, String name, List<Ingredient> ingredients, String recipe) {
//        this.id = id;
//        this.name = name;
//        this.ingredients = ingredients;
//        this.recipe = recipe;
//    }

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
