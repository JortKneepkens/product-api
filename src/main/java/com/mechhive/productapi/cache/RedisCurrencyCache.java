package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisCurrencyCache {

    private static final String KEY = "rates";

    private final RedisTemplate<String, Object> redis;
    private final RedisTypeSafeAccessor accessor;

    public RedisCurrencyCache(
            RedisTemplate<String, Object> redis,
            RedisTypeSafeAccessor accessor
    ) {
        this.redis = redis;
        this.accessor = accessor;
    }

    public void save(Map<String, Double> rates) {
        redis.opsForValue().set(KEY, rates);
    }

    public Map<String, Double> get() {
        Object raw = redis.opsForValue().get(KEY);
        if (raw == null) return null;
        return accessor.read(raw, new TypeReference<Map<String, Double>>() {});
    }
}
