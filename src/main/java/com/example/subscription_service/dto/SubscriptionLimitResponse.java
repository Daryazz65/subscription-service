package com.example.subscription_service.dto;

import com.example.subscription_service.domain.SubscriptionType;

import java.time.Instant;

public record SubscriptionLimitResponse(
        String userLogin,
        SubscriptionType subscriptionType,
        Instant expiresAt,
        long maxFileSizeBytes
) {
}