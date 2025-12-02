package com.mechhive.productapi.controller;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.service.ProductService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    RedisTemplate<String, Object> redisTemplate;

    public ProductController(ProductService service,
                             RedisTemplate<String, Object> redis) {
        this.service = service;
        this.redisTemplate = redis;
    }

    @GetMapping
    public List<EnrichedProduct> getProducts(
            @RequestParam(defaultValue = "eur") String currency
    ) {
        return service.getAllEnriched(currency);
    }

    @GetMapping("/debug/cache")
    public Map<String, Object> debug() {
        return Map.of(
                "productsKeyExists", redisTemplate.hasKey("products"),
                "ratesKeyExists", redisTemplate.hasKey("rates")
        );
    }
}
