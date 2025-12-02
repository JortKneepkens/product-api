package com.mechhive.productapi.repository;


import com.mechhive.productapi.cache.RedisCurrencyCache;
import com.mechhive.productapi.client.CurrencyApiClient;
import com.mechhive.productapi.error.UnsupportedCurrencyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class CurrencyRepositoryTest {

    @Mock
    private CurrencyApiClient client;

    @Mock
    private RedisCurrencyCache cache;

    @InjectMocks
    private CurrencyRepository repository;

    @Test
    void getRate_shouldReturnBigDecimalFromCache() {
        when(cache.getRate("USD")).thenReturn(1.2d);

        BigDecimal rate = repository.getRate("USD");

        assertThat(rate).isEqualByComparingTo("1.2");
        verify(cache).getRate("USD");
    }

    @Test
    void getRate_shouldThrowForUnsupportedCurrency() {
        when(cache.getRate("ABC")).thenReturn(null);

        assertThatThrownBy(() -> repository.getRate("ABC"))
                .isInstanceOf(UnsupportedCurrencyException.class)
                .hasMessageContaining("ABC");
    }

    @Test
    void refresh_shouldFetchFromApiAndStoreInCache() {
        Map<String, Double> apiRates = Map.of("usd", 1.2, "jpy", 160.0);
        when(client.fetchRates()).thenReturn(apiRates);

        repository.refresh();

        ArgumentCaptor<Map<String, Double>> captor = ArgumentCaptor.forClass(Map.class);
        verify(cache).save(captor.capture());

        assertThat(captor.getValue())
                .containsEntry("usd", 1.2)
                .containsEntry("jpy", 160.0);
    }
}