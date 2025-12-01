package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTypeSafeAccessor {

    private final ObjectMapper mapper;

    public RedisTypeSafeAccessor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T getAs(RedisTemplate<String, Object> redis, String key, TypeReference<T> typeRef) {
        Object raw = redis.opsForValue().get(key);
        if (raw == null) return null;
        return mapper.convertValue(raw, typeRef);
    }
}
