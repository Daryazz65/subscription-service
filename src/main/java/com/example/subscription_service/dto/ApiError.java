package com.example.subscription_service.dto;

import java.time.Instant;

public record ApiError(
        Instant timestamp,
        int status,
        String message
) {
}