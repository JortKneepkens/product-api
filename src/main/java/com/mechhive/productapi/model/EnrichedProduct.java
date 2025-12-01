package com.mechhive.productapi.model;

import java.math.BigDecimal;

public record EnrichedProduct(
        Long id,
        String title,
        String description,
        BigDecimal purchasePrice, // before fee, in requested currency
        BigDecimal salePrice,     // after fee, in requested currency
        String currency
) {
}
