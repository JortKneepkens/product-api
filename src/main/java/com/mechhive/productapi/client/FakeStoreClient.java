package com.mechhive.productapi.client;

import com.mechhive.productapi.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class FakeStoreClient {

    private final RestTemplate rest;

    public FakeStoreClient(RestTemplateBuilder builder) {
        this.rest = builder.build();
    }

    public List<Product> fetchProducts() {
        Product[] response = rest.getForObject(
                "https://fakestoreapi.com/products",
                Product[].class
        );

        if (response == null) {
            throw new IllegalStateException("Fake Store API returned invalid or empty data");
        }

        return Arrays.asList(response);
    }
}
