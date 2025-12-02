package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (rates == null || rates.isEmpty()) {
            return;
        }

        // Normalize keys to lowercase
        Map<String, Double> normalized = rates.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toLowerCase(Locale.ROOT),
                        Map.Entry::getValue
                ));

        redis.<String, Object>opsForHash().putAll(KEY, normalized);
    }

    public Double getRate(String currencyCode) {
        String field = currencyCode.toLowerCase(Locale.ROOT);
        Object raw = redis.opsForHash().get(KEY, field);
        if (raw == null) {
            return null;
        }
        return accessor.read(raw, new TypeReference<Double>() {});
    }

    public Map<String, Double> getAll() {
        Map<Object, Object> raw = redis.opsForHash().entries(KEY);
        if (raw.isEmpty()) {
            return Map.of();
        }

        return raw.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> (String) e.getKey(),
                        e -> accessor.read(e.getValue(), new TypeReference<Double>() {})
                ));
    }
}
