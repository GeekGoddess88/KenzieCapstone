package com.kenzie.appserver.service;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ValidationException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -567012686821289070L;
    private final HttpStatus statusCode;

    public ValidationException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}