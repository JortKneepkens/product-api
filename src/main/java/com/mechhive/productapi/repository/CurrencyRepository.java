package com.mechhive.productapi.repository;

import com.mechhive.productapi.cache.RedisCurrencyCache;
import com.mechhive.productapi.client.CurrencyApiClient;
import com.mechhive.productapi.error.UnsupportedCurrencyException;
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

    /**
     * Domain-friendly access to all rates as BigDecimal.
     * Currently not used
     */
    public Map<String, BigDecimal> getRates() {
        Map<String, Double> raw = cache.getAll();
        if (raw == null || raw.isEmpty()) {
            return Map.of();
        }

        return raw.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue())
                ));
    }

    public BigDecimal getRate(String currencyCode) {
        Double raw = cache.getRate(currencyCode);
        if (raw == null) {
            throw new UnsupportedCurrencyException(currencyCode);
        }
        return BigDecimal.valueOf(raw);
    }

    public void refresh() {
        Map<String, Double> rawRates = client.fetchRates();
        cache.save(rawRates);
    }
}
