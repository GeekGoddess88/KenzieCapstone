package com.kenzie.capstone.service.model;

import java.util.List;

public interface DrinkInterface {
    public String getId();
    public String getName();
    public String getRecipe();
    public List<IngredientInterface> getIngredients();

    void setId(String id);
    void setName(String name);
    void setRecipe(String recipe);
    void setIngredients(List<IngredientInterface> ingredients);

}
