package com.example.subscription_service.controller;

import com.example.subscription_service.dto.ApiError;
import com.example.subscription_service.exception.SubscriptionException;
import com.example.subscription_service.exception.SubscriptionNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            SubscriptionNotFoundException exception
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(
            ConstraintViolationException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(SubscriptionException.class)
    public ResponseEntity<ApiError> handleSubscription(
            SubscriptionException exception
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

    private ResponseEntity<ApiError> buildResponse(
            HttpStatus status,
            String message
    ) {
        ApiError response = new ApiError(
                Instant.now(),
                status.value(),
                message
        );

        return ResponseEntity.status(status).body(response);
    }
}