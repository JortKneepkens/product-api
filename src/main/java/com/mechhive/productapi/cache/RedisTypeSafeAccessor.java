package com.mechhive.productapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RedisTypeSafeAccessor {

    private final ObjectMapper mapper;

    public RedisTypeSafeAccessor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T read(Object raw, TypeReference<T> type) {
        return mapper.convertValue(raw, type);
    }
}