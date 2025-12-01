package com.mechhive.productapi.model;

import java.math.BigDecimal;
import java.util.List;

public record Transaction(
        String id,
        List<EnrichedProduct> items,
        BigDecimal total,
        String currency
) {
}
