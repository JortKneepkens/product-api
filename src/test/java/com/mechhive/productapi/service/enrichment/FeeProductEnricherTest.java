package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


class FeeProductEnricherTest {

    private final FeeProductEnricher enricher = new FeeProductEnricher();

    @Test
    void enrich_shouldApplyTenPercentFeeAndRoundToTwoDecimals() {
        EnrichedProduct base = new EnrichedProduct(
                1L,
                "Test",
                "desc",
                new BigDecimal("100.00"),
                new BigDecimal("100.00"),
                "EUR"
        );
        ProductEnrichmentContext ctx = new ProductEnrichmentContext("eur", new BigDecimal("1.0"));

        EnrichedProduct result = enricher.enrich(base, ctx);

        // purchase unchanged but scaled
        assertThat(result.purchasePrice()).isEqualByComparingTo("100.00");
        // sale should be +10%
        assertThat(result.salePrice()).isEqualByComparingTo("110.00");
        assertThat(result.currency()).isEqualTo("EUR");
    }
}