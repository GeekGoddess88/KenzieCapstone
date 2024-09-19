package com.kenzie.capstone.service.model;

import com.kenzie.capstone.service.model.IngredientRecord;

public class IngredientCreateRequest {
    private String id;
    private String name;
    private int quantity;

    public IngredientCreateRequest() {}

    public IngredientCreateRequest(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public IngredientRecord toIngredientRecord(String id, String name, int quantity) {
        return new IngredientRecord(id, name, quantity);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
