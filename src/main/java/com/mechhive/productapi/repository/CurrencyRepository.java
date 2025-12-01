package com.mechhive.productapi.repository;

import com.mechhive.productapi.cache.RedisCurrencyCache;
import com.mechhive.productapi.client.CurrencyApiClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CurrencyRepository {

    private final CurrencyApiClient client;
    private final RedisCurrencyCache cache;

    public CurrencyRepository(CurrencyApiClient client, RedisCurrencyCache cache) {
        this.client = client;
        this.cache  = cache;
    }

    public Map<String, BigDecimal> getRates() {
        var cached = cache.get();
        return cached != null ? cached : Map.of();
    }

    public BigDecimal getRate(String currencyCode) {
        String key = currencyCode.toLowerCase(Locale.ROOT);
        var rates = getRates();
        if (!rates.containsKey(key)) {
            throw new IllegalArgumentException("Unsupported currency: " + currencyCode);
        }
        return rates.get(key);
    }

    public void refresh() {
        Map<String, Double> rawRates = client.fetchRates();
        Map<String, BigDecimal> rates = rawRates.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue())
                ));
        cache.save(rates);
    }
}
