package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mechhive.productapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisProductCache {

    private static final String KEY = "products";

    private final RedisTemplate<String, Object> redis;
    private final RedisTypeSafeAccessor accessor;

    public RedisProductCache(
            RedisTemplate<String, Object> redis,
            RedisTypeSafeAccessor accessor
    ) {
        this.redis = redis;
        this.accessor = accessor;
    }

    public void save(List<Product> products) {
        redis.opsForValue().set(KEY, products);
    }

    public List<Product> get() {
        return accessor.getAs(redis, KEY, new TypeReference<List<Product>>() {});
    }
}
