package com.mechhive.productapi.service;

import com.mechhive.productapi.model.EnrichedProduct;
import com.mechhive.productapi.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransaction_shouldSumSalePricesAndReturnTransaction() {
        List<Long> ids = List.of(1L, 2L);

        EnrichedProduct p1 = new EnrichedProduct(1L, "A", "d1",
                new BigDecimal("10.00"), new BigDecimal("11.00"), "EUR");
        EnrichedProduct p2 = new EnrichedProduct(2L, "B", "d2",
                new BigDecimal("20.00"), new BigDecimal("22.00"), "EUR");

        when(productService.getEnrichedByIds(ids, "eur"))
                .thenReturn(List.of(p1, p2));

        Transaction tx = transactionService.createTransaction(ids, "eur");

        assertThat(tx.id()).isNotBlank();
        assertThat(tx.items()).containsExactly(p1, p2);
        assertThat(tx.total()).isEqualByComparingTo("33.00"); // 11 + 22
        assertThat(tx.currency()).isEqualTo("EUR");
    }
}