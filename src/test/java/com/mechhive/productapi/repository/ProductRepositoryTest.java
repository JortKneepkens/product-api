package com.mechhive.productapi.repository;

import com.mechhive.productapi.cache.RedisProductCache;
import com.mechhive.productapi.client.FakeStoreClient;
import com.mechhive.productapi.error.ProductNotFoundException;
import com.mechhive.productapi.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private FakeStoreClient client;

    @Mock
    private RedisProductCache cache;

    @InjectMocks
    private ProductRepository repository;

    @Test
    void refresh_shouldFetchProductsFromClientAndStoreInCache() {
        Product p1 = new Product(1L, "A", "d1", new BigDecimal("10.00"));
        Product p2 = new Product(2L, "B", "d2", new BigDecimal("20.00"));

        when(client.fetchProducts()).thenReturn(List.of(p1, p2));

        repository.refresh();

        verify(cache).saveAll(argThat(list ->
                list.size() == 2 &&
                list.contains(p1) &&
                list.contains(p2)
        ));
    }

    @Test
    void findAll_shouldReturnAllProductsFromCache() {
        Product p1 = new Product(1L, "A", "d1", new BigDecimal("10.00"));
        Product p2 = new Product(2L, "B", "d2", new BigDecimal("20.00"));

        when(cache.getAll()).thenReturn(List.of(p1, p2));

        List<Product> result = repository.findAll();

        assertThat(result).containsExactly(p1, p2);
    }

    @Test
    void findByIds_shouldReturnProductsWhenAllExist() {
        Product p1 = new Product(1L, "A", "d1", new BigDecimal("10.00"));
        Product p2 = new Product(2L, "B", "d2", new BigDecimal("20.00"));

        when(cache.getMany(argThat(ids ->
                ids.contains(1L) && ids.contains(2L)
        ))).thenReturn(List.of(p1, p2));

        List<Product> result = repository.findByIds(List.of(1L, 2L));

        assertThat(result).containsExactly(p1, p2);
    }

    @Test
    void findByIds_shouldThrowProductNotFoundExceptionWhenAnyProductMissing() {
        Product p1 = new Product(1L, "A", "d1", new BigDecimal("10.00"));

        when(cache.getMany(argThat(ids ->
                ids.contains(1L) && ids.contains(2L)
        ))).thenReturn(List.of(p1));

        assertThatThrownBy(() -> repository.findByIds(List.of(1L, 2L)))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("2");
    }
}