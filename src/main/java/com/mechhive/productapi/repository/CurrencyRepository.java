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
        Map<String, Double> raw = cache.get();
        if (raw == null) return Map.of();

        return raw.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue())
                ));
    }

    public BigDecimal getRate(String currencyCode) {
        String key = currencyCode.toLowerCase(Locale.ROOT);
        Map<String, BigDecimal> rates = getRates();

        BigDecimal rate = rates.get(key);
        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency: " + currencyCode);
        }
        return rate;
    }

    public void refresh() {
        Map<String, Double> rawRates = client.fetchRates();
        cache.save(rawRates);
    }
}
