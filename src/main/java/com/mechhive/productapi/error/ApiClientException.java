package com.mechhive.productapi.error;

public abstract class ApiClientException extends RuntimeException {

    private final int statusCode;

    protected ApiClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }
}
