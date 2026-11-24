package com.example.subscription_service.service;

import com.example.subscription_service.config.AppProperties;
import com.example.subscription_service.domain.EventType;
import com.example.subscription_service.domain.OutboxMessage;
import com.example.subscription_service.domain.SubscriptionType;
import com.example.subscription_service.domain.UserSubscription;
import com.example.subscription_service.dto.SubscriptionChangedEvent;
import com.example.subscription_service.dto.SubscriptionLimitResponse;
import com.example.subscription_service.exception.EventSerializationException;
import com.example.subscription_service.exception.SubscriptionNotFoundException;
import com.example.subscription_service.repository.OutboxMessageRepository;
import com.example.subscription_service.repository.UserSubscriptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final OutboxMessageRepository outboxMessageRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final ObjectMapper objectMapper;
    private final AppProperties properties;

    @Transactional(readOnly = true)
    public SubscriptionLimitResponse getLimit(String userLogin) {
        UserSubscription subscription = findSubscription(userLogin);

        long maxFileSizeBytes = resolveMaxFileSizeBytes(
                subscription,
                Instant.now()
        );

        return subscriptionMapper.toLimitResponse(
                subscription,
                maxFileSizeBytes
        );
    }

    @Transactional
    public void downgradeExpiredSubscriptions() {
        Instant currentTime = Instant.now();

        List<UserSubscription> subscriptions =
                userSubscriptionRepository.findExpiredForUpdate(
                        SubscriptionType.PAID,
                        currentTime
                );

        for (UserSubscription subscription : subscriptions) {
            subscription.downgradeToFree();
            saveSubscriptionChangedEvent(subscription);
        }
    }

    private UserSubscription findSubscription(String userLogin) {
        return userSubscriptionRepository.findById(userLogin)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException(userLogin)
                );
    }

    private long resolveMaxFileSizeBytes(
            UserSubscription subscription,
            Instant currentTime
    ) {
        if (subscription.hasActivePaidSubscription(currentTime)) {
            return Long.MAX_VALUE;
        }

        return properties.subscription().freeMaxFileSizeBytes();
    }

    private void saveSubscriptionChangedEvent(
            UserSubscription subscription
    ) {
        UUID eventId = UUID.randomUUID();

        SubscriptionChangedEvent event =
                subscriptionMapper.toChangedEvent(
                        eventId,
                        subscription
                );

        try {
            String payload = objectMapper.writeValueAsString(event);

            OutboxMessage outboxMessage = new OutboxMessage(
                    eventId,
                    subscription.getUserLogin(),
                    EventType.SUBSCRIPTION_CHANGED,
                    properties.topics().subscriptionChanged(),
                    payload
            );

            outboxMessageRepository.save(outboxMessage);
        } catch (JsonProcessingException exception) {
            throw new EventSerializationException(
                    "Cannot serialize subscription changed event",
                    exception
            );
        }
    }
}