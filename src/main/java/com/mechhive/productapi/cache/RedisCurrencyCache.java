package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    public void save(Map<String, BigDecimal> rates) {
        redis.opsForValue().set(KEY, rates);
    }

    public Map<String, BigDecimal> get() {
        return accessor.getAs(redis, KEY, new TypeReference<Map<String, BigDecimal>>() {});
    }
}
