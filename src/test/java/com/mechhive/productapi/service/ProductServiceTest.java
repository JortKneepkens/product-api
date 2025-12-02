package com.mechhive.productapi.service;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Product;
import com.mechhive.productapi.repository.CurrencyRepository;
import com.mechhive.productapi.repository.ProductRepository;
import com.mechhive.productapi.service.enrichment.ProductEnrichmentContext;
import com.mechhive.productapi.service.enrichment.ProductEnrichmentPipeline;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ProductEnrichmentPipeline pipeline;

    @InjectMocks
    private ProductService service;

    @Test
    void getAllEnriched_shouldFetchProductsApplyFxAndPipeline() {
        Product p1 = new Product(1L, "A", "d1", new BigDecimal("10.00"));
        Product p2 = new Product(2L, "B", "d2", new BigDecimal("20.00"));
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));
        when(currencyRepository.getRate("USD")).thenReturn(new BigDecimal("1.5"));

        EnrichedProduct e1 = new EnrichedProduct(1L, "A", "d1",
                new BigDecimal("15.00"), new BigDecimal("16.50"), "USD");
        EnrichedProduct e2 = new EnrichedProduct(2L, "B", "d2",
                new BigDecimal("30.00"), new BigDecimal("33.00"), "USD");

        when(pipeline.enrich(eq(p1), any(ProductEnrichmentContext.class))).thenReturn(e1);
        when(pipeline.enrich(eq(p2), any(ProductEnrichmentContext.class))).thenReturn(e2);

        List<EnrichedProduct> result = service.getAllEnriched("USD");

        assertThat(result).containsExactly(e1, e2);

        ArgumentCaptor<ProductEnrichmentContext> ctxCaptor =
                ArgumentCaptor.forClass(ProductEnrichmentContext.class);
        verify(pipeline, times(2)).enrich(any(Product.class), ctxCaptor.capture());
        assertThat(ctxCaptor.getValue().fxRate()).isEqualByComparingTo("1.5");
    }
}