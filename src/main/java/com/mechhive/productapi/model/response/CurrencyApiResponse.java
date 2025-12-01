package com.mechhive.productapi.model.response;

import java.util.Map;

public record CurrencyApiResponse(
        Map<String, Double> rates
) {}
