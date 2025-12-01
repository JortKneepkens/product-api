package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeeProductEnricher implements ProductEnricherInterface {

    private static final BigDecimal FEE_MULTIPLIER = new BigDecimal("1.10");

    @Override
    public EnrichedProduct enrich(EnrichedProduct product, ProductEnrichmentContext context) {
        BigDecimal sale = product.purchasePrice()
                .multiply(FEE_MULTIPLIER)
                .setScale(2, RoundingMode.HALF_UP);

        return new EnrichedProduct(
                product.id(),
                product.title(),
                product.description(),
                product.purchasePrice().setScale(2, RoundingMode.HALF_UP),
                sale,
                product.currency()
        );
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
