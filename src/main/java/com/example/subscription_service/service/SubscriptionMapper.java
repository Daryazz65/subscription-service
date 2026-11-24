package com.example.subscription_service.service;

import com.example.subscription_service.domain.UserSubscription;
import com.example.subscription_service.dto.SubscriptionChangedEvent;
import com.example.subscription_service.dto.SubscriptionLimitResponse;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public SubscriptionLimitResponse toLimitResponse(
            UserSubscription subscription,
            long maxFileSizeBytes
    ) {
        return new SubscriptionLimitResponse(
                subscription.getUserLogin(),
                subscription.getSubscriptionType(),
                subscription.getExpiresAt(),
                maxFileSizeBytes
        );
    }

    public SubscriptionChangedEvent toChangedEvent(
            UUID eventId,
            UserSubscription subscription
    ) {
        return new SubscriptionChangedEvent(
                eventId,
                subscription.getUserLogin(),
                subscription.getSubscriptionType(),
                Instant.now()
        );
    }
}