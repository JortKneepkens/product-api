package com.mechhive.productapi.model;

import java.math.BigDecimal;

public record Product(
        Long id,
        String title,
        String description,
        BigDecimal price // raw EUR price
) {
}
