package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Product;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ProductEnrichmentPipeline {

    private final BaseProductEnricher baseEnricher;
    private final List<ProductEnricherInterface> enrichers;

    public ProductEnrichmentPipeline(
            BaseProductEnricher baseEnricher,
            List<ProductEnricherInterface> enrichers
    ) {
        this.baseEnricher = baseEnricher;
        this.enrichers = enrichers.stream()
                .sorted(Comparator.comparingInt(ProductEnricherInterface::getOrder))
                .toList();
    }

    public EnrichedProduct enrich(Product product, ProductEnrichmentContext ctx) {
        EnrichedProduct current = baseEnricher.from(product, ctx);
        for (ProductEnricherInterface enricher : enrichers) {
            current = enricher.enrich(current, ctx);
        }
        return current;
    }
}
