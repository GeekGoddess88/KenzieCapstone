package com.kenzie.capstone.service.model;

public class DeleteIngredientResponse {

    private String id;
    private String message;

    public DeleteIngredientResponse() {}

    public DeleteIngredientResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
