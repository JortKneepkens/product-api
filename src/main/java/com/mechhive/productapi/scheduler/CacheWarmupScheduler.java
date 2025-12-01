package com.mechhive.productapi.scheduler;

import com.mechhive.productapi.repository.CurrencyRepository;
import com.mechhive.productapi.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CacheWarmupScheduler {

    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;

    public CacheWarmupScheduler(ProductRepository productRepository, CurrencyRepository currencyRepository) {
        this.productRepository = productRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    @Scheduled(fixedRate = 300_000) // Exactly every 5 minutes
    public void refresh() {
        productRepository.refresh();
        currencyRepository.refresh();
    }
}
