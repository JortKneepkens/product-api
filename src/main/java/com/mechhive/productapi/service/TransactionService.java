package com.mechhive.productapi.service;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionService {
    private final ProductService productService;

    public TransactionService(ProductService productService) {
        this.productService = productService;
    }

    public Transaction createTransaction(List<Long> productIds, String currency) {
        List<EnrichedProduct> items = productService.getEnrichedByIds(productIds, currency);

        BigDecimal total = items.stream()
                .map(EnrichedProduct::salePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return new Transaction(
                UUID.randomUUID().toString(),
                items,
                total,
                currency.toUpperCase()
        );
    }
}
