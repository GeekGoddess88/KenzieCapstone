package com.kenzie.capstone.service.model;

public class IngredientUpdateRequest {

    private String name;
    private int quantity;

    public IngredientUpdateRequest() {}

    public IngredientUpdateRequest(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public IngredientRecord toIngredientRecord(String id) {
        return new IngredientRecord(id, name, quantity);
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
