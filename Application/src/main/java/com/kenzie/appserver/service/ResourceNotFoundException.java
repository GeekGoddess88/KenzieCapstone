package com.kenzie.appserver.service;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResourceNotFoundException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -976539735662822088L;
    private final HttpStatus statusCode;

    public ResourceNotFoundException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
