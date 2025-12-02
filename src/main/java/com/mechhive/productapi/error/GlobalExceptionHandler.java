package com.mechhive.productapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 400,
                        "error", message
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonParse(HttpMessageNotReadableException ex) {

        String message = "Malformed JSON request: " + extractCause(ex);

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", 400,
                        "error", message
                )
        );
    }

    private String extractCause(HttpMessageNotReadableException ex) {
        if (ex.getCause() != null) {
            return ex.getCause().getMessage();
        }
        return "Invalid JSON structure";
    }
}
