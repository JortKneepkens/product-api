package com.mechhive.productapi.model.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        TransactionRequest req = new TransactionRequest(List.of(1L, 2L, 3L), "eur");

        var violations = validator.validate(req);

        assertThat(violations).isEmpty();
    }

    @Test
    void negativeProductIdShouldFailValidation() {
        TransactionRequest req = new TransactionRequest(List.of(1L, -5L), "eur");

        var violations = validator.validate(req);

        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().contains("productIds") &&
                        v.getMessage().contains("positive"));
    }

    @Test
    void emptyProductListShouldFailValidation() {
        TransactionRequest req = new TransactionRequest(List.of(), "eur");

        var violations = validator.validate(req);

        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("productIds"));
    }

    @Test
    void blankCurrencyShouldFailValidation() {
        TransactionRequest req = new TransactionRequest(List.of(1L), "  ");

        var violations = validator.validate(req);

        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("currency"));
    }
}