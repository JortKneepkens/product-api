package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BaseProductEnricherTest {

    private final BaseProductEnricher enricher = new BaseProductEnricher();

    @Test
    void from_shouldCreateBaseEnrichedProductWithFxApplied() {
        Product product = new Product(1L, "Test", "desc", new BigDecimal("100.00"));
        ProductEnrichmentContext ctx = new ProductEnrichmentContext("usd", new BigDecimal("1.2"));

        EnrichedProduct result = enricher.from(product, ctx);

        // purchase price should be price * fx
        assertThat(result.purchasePrice()).isEqualByComparingTo("120.00");
        // initial sale price equals purchase before fee enricher
        assertThat(result.salePrice()).isEqualByComparingTo("120.00");
        assertThat(result.currency()).isEqualTo("USD");
    }
}