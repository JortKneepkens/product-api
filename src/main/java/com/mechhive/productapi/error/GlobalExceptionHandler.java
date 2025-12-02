package com.mechhive.productapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiClientException.class)
    public ResponseEntity<Object> handleApiClientException(ApiClientException ex) {

        return ResponseEntity
                .status(ex.statusCode())
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", ex.statusCode(),
                        "error", ex.getMessage()
                ));
    }
}
