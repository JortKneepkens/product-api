package com.mechhive.productapi.repository;

import com.mechhive.productapi.cache.RedisProductCache;
import com.mechhive.productapi.client.FakeStoreClient;
import com.mechhive.productapi.error.ProductNotFoundException;
import com.mechhive.productapi.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final FakeStoreClient client;
    private final RedisProductCache cache;

    public ProductRepository(FakeStoreClient client, RedisProductCache cache) {
        this.client = client;
        this.cache  = cache;
    }

    public List<Product> findAll() {
        return cache.getAll();
    }

    public List<Product> findByIds(Collection<Long> ids) {
        List<Product> products = cache.getMany(ids);

        if (products.size() != ids.size()) {
            handleMissingProduct(ids, products);
        }
        return products;
    }

    /** Called by scheduler on startup + every 5 minutes */
    public void refresh() {
        List<Product> apiProducts = client.fetchProducts().stream()
                .map(p -> new Product(
                        p.id(),
                        p.title(),
                        p.description(),
                        p.price()
                ))
                .toList();

        cache.saveAll(apiProducts);
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
