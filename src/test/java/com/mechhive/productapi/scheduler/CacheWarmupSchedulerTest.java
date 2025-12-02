package com.mechhive.productapi.scheduler;

import com.mechhive.productapi.repository.CurrencyRepository;
import com.mechhive.productapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CacheWarmupSchedulerTest {

    @Test
    void refreshShouldCallBothRepositories() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);

        CacheWarmupScheduler scheduler = new CacheWarmupScheduler(productRepository, currencyRepository);

        scheduler.refresh();

        verify(productRepository).refresh();
        verify(currencyRepository).refresh();
    }
}