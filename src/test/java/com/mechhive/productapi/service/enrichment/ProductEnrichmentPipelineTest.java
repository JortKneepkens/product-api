package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductEnrichmentPipelineTest {

    @Test
    void enrich_shouldApplyBaseAndFeeEnrichersInOrder() {
        BaseProductEnricher base = new BaseProductEnricher();
        FeeProductEnricher fee = new FeeProductEnricher();
        ProductEnrichmentPipeline pipeline =
                new ProductEnrichmentPipeline(base, List.of(fee));

        Product p = new Product(1L, "Test", "desc", new BigDecimal("100.00"));
        ProductEnrichmentContext ctx = new ProductEnrichmentContext("eur", new BigDecimal("2.0"));

        EnrichedProduct result = pipeline.enrich(p, ctx);

        // fx = 2.0 => purchase = 200
        assertThat(result.purchasePrice()).isEqualByComparingTo("200.00");
        // sale = +10% => 220
        assertThat(result.salePrice()).isEqualByComparingTo("220.00");
        assertThat(result.currency()).isEqualTo("EUR");
    }
}