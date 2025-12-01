package com.mechhive.productapi.controller;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<EnrichedProduct> getProducts(
            @RequestParam(defaultValue = "eur") String currency
    ) {
        return service.getAllEnriched(currency);
    }
}
