package com.mechhive.productapi.error;

public class ProductNotFoundException extends ApiClientException {
    public ProductNotFoundException(long id) {
        super("Product not found: " + id, 404);
    }
}
