package com.kenzie.appserver.service.model;

public class Ingredient {
    private final String id;
    private final String name;
    private final int quantity;

    public Ingredient(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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
