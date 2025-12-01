package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BaseProductEnricher {
    public EnrichedProduct from(Product product, ProductEnrichmentContext ctx) {
        BigDecimal purchase = product.price().multiply(ctx.fxRate());
        return new EnrichedProduct(
                product.id(),
                product.title(),
                product.description(),
                purchase,
                purchase,
                ctx.normalizedCurrency()
        );
    }
}
