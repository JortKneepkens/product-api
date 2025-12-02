package com.mechhive.productapi.error;

public class UnsupportedCurrencyException extends ApiClientException {
    public UnsupportedCurrencyException(String currency) {
        super("Unsupported currency: " + currency.toUpperCase(), 400);
    }
}
