package com.kenzie.capstone.service.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteDrinkResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("message")
    private String message;

    public DeleteDrinkResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
