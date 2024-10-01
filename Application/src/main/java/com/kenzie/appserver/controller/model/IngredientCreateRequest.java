package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class IngredientCreateRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;
    @JsonProperty("quantity")
    private int quantity;

//    public IngredientCreateRequest() {}
//
//    public IngredientCreateRequest(String name, int quantity) {
//        this.name = name;
//        this.quantity = quantity;
//    }

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
