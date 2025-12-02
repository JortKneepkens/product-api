package com.mechhive.productapi.service;

import com.mechhive.productapi.error.ProductNotFoundException;
import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Product;
import com.mechhive.productapi.repository.CurrencyRepository;
import com.mechhive.productapi.repository.ProductRepository;
import com.mechhive.productapi.service.enrichment.ProductEnrichmentContext;
import com.mechhive.productapi.service.enrichment.ProductEnrichmentPipeline;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;
    private final ProductEnrichmentPipeline pipeline;

    public ProductService(
            ProductRepository productRepository,
            CurrencyRepository currencyRepository,
            ProductEnrichmentPipeline pipeline
    ) {
        this.productRepository = productRepository;
        this.currencyRepository = currencyRepository;
        this.pipeline = pipeline;
    }

    public List<EnrichedProduct> getAllEnriched(String currencyCode) {
        List<Product> products = productRepository.findAll();
        return enrichProducts(products, currencyCode);
    }

    public List<EnrichedProduct> getEnrichedByIds(Collection<Long> ids, String currencyCode) {
        List<Product> products = productRepository.findByIds(ids);

        if (products.size() != ids.size()) {
            handleMissingProduct(ids, products);
        }

        return enrichProducts(products, currencyCode);
    }

    private List<EnrichedProduct> enrichProducts(List<Product> products, String currencyCode) {
        BigDecimal fxRate = currencyRepository.getRate(currencyCode);

        ProductEnrichmentContext ctx = new ProductEnrichmentContext(currencyCode, fxRate);

        return products.stream()
                .map(p -> pipeline.enrich(p, ctx))
                .toList();
    }

    private void handleMissingProduct(Collection<Long> requestedIds, List<Product> foundProducts) {
        // Determine which ID is missing
        var foundIds = foundProducts.stream()
                .map(Product::id)
                .collect(Collectors.toSet());

        requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .findFirst()
                .ifPresent(missing -> {
                    throw new ProductNotFoundException(missing);
                });
    }
}
