package com.mechhive.productapi.controller;

import com.mechhive.productapi.model.Transaction;
import com.mechhive.productapi.model.request.TransactionRequest;
import com.mechhive.productapi.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public Transaction create(@RequestBody TransactionRequest req) {
        return service.createTransaction(req.productIds(), req.currency());
    }
}
