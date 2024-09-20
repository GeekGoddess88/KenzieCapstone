package com.kenzie.capstone.service.model;

public class DeleteDrinkResponse {

    private String id;
    private String message;

    public DeleteDrinkResponse() {}

    public DeleteDrinkResponse(String id, String message) {
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
