package com.mechhive.productapi.service.enrichment;

import com.mechhive.productapi.model.EnrichedProduct;

public interface ProductEnricherInterface {
    /**
     * Takes an (intermediate) EnrichedProduct and returns an updated one.
     */
    EnrichedProduct enrich(EnrichedProduct product, ProductEnrichmentContext context);

    default int getOrder() {
        return 0;
    }
}
