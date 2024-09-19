package com.kenzie.capstone.service.model;

import com.kenzie.capstone.service.model.IngredientRecord;

public class IngredientResponse {
    private String id;
    private String name;
    private int quantity;

    public IngredientResponse(IngredientRecord ingredientRecord) {
        this.id = ingredientRecord.getId();
        this.name = ingredientRecord.getName();
        this.quantity = ingredientRecord.getQuantity();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

}
