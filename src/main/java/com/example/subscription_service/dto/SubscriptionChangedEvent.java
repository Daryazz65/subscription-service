package com.example.subscription_service.dto;

import com.example.subscription_service.domain.SubscriptionType;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionChangedEvent(
        UUID eventId,
        String userLogin,
        SubscriptionType subscriptionType,
        Instant occurredAt
) {
}
