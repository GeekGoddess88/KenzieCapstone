package com.kenzie.appserver.service.model;

import com.kenzie.capstone.service.model.IngredientInterface;

import java.util.Objects;

public class Ingredient implements IngredientInterface {
    public String id;
    public String name;
    public int quantity;

    public Ingredient() {}

    public Ingredient(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String getId() { return id; }
    @Override
    public String getName() { return name; }
    @Override
    public int getQuantity() { return quantity; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
