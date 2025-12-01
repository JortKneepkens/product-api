package com.mechhive.productapi.client;

import com.mechhive.productapi.model.response.CurrencyApiResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Component
public class CurrencyApiClient {

    private final RestTemplate rest;

    public CurrencyApiClient(RestTemplateBuilder builder) {
        this.rest = builder.build();
    }

    public Map<String, Double> fetchRates() {
        CurrencyApiResponse response = rest.getForObject(
                "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json",
                CurrencyApiResponse.class
        );

        return Optional.ofNullable(response)
                .map(CurrencyApiResponse::rates)
                .orElseThrow(() -> new IllegalStateException("Currency API returned invalid data"));
    }
}
