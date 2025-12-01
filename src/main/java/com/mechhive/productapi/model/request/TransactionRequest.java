package com.mechhive.productapi.model.request;

import java.util.List;

public record TransactionRequest(
        List<Long> productIds,
        String currency
) {

}
