package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mechhive.productapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class RedisProductCache {

    private static final String INDEX_KEY = "products:index";
    private static final String PRODUCT_KEY_PREFIX = "product:";

    private final RedisTemplate<String, Object> redis;
    private final RedisTypeSafeAccessor accessor;

    public RedisProductCache(
            RedisTemplate<String, Object> redis,
            RedisTypeSafeAccessor accessor
    ) {
        this.redis = redis;
        this.accessor = accessor;
    }

    private String key(long id) {
        return PRODUCT_KEY_PREFIX + id;
    }

    /** Save all products in individual Redis keys, and update index */
    public void saveAll(List<Product> products) {
        if (products == null || products.isEmpty()) return;

        // store each product individually
        products.forEach(p ->
                redis.opsForValue().set(key(p.id()), p)
        );

        // update the index of product IDs
        List<String> ids = products.stream()
                .map(p -> String.valueOf(p.id()))
                .toList();

        redis.delete(INDEX_KEY);
        redis.opsForList().rightPushAll(INDEX_KEY, ids.toArray(new String[0]));
    }

    /** Get one product by ID */
    public Product get(long id) {
        Object raw = redis.opsForValue().get(key(id));
        if (raw == null) return null;
        return accessor.read(raw, new TypeReference<Product>() {});
    }

    /** Get multiple products */
    public List<Product> getMany(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        List<String> keys = ids.stream()
                .map(this::key)
                .toList();

        List<Object> rawList = redis.opsForValue().multiGet(keys);
        if (rawList == null) return List.of();

        return rawList.stream()
                .filter(Objects::nonNull)
                .map(raw -> accessor.read(raw, new TypeReference<Product>() {}))
                .toList();
    }

    /** Get ALL product IDs from index and load them via MGET */
    public List<Product> getAll() {
        List<Object> idList = redis.opsForList().range(INDEX_KEY, 0, -1);
        if (idList == null || idList.isEmpty()) return List.of();

        List<Long> ids = idList.stream()
                .map(o -> Long.valueOf(o.toString()))
                .toList();

        return getMany(ids);
    }
}
