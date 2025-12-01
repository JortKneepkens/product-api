package com.mechhive.productapi.service.enrichment;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public record ProductEnrichmentContext(
        String targetCurrency,
        BigDecimal fxRate,
        Map<String, Object> metadata
) {
    public ProductEnrichmentContext(String targetCurrency, BigDecimal fxRate) {
        this(targetCurrency, fxRate, Map.of());
    }

    public String normalizedCurrency() {
        return targetCurrency.toUpperCase(Locale.ROOT);
    }
}
