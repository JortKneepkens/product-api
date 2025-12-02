package com.mechhive.productapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record TransactionRequest(
        @NotEmpty(message = "productIds cannot be empty")
        List< @NotNull(message = "productId cannot be null")
        @Positive(message = "productId must be a positive integer")
            Long> productIds,

        @NotBlank(message = "currency is required")
        String currency
) {

}
