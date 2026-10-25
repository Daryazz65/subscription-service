package com.example.subscription_service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.subscription_service.domain.SubscriptionType;
import com.example.subscription_service.domain.UserSubscription;
import com.example.subscription_service.dto.SubscriptionLimitResponse;
import com.example.subscription_service.repository.OutboxMessageRepository;
import com.example.subscription_service.repository.UserSubscriptionRepository;
import com.example.subscription_service.service.SubscriptionService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SubscriptionExpirationIT extends AbstractIT {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @BeforeEach
    void cleanDatabase() {
        outboxMessageRepository.deleteAll();
        userSubscriptionRepository.deleteAll();
    }

    @Test
    void downgradesExpiredPaidSubscriptionAndCreatesOutboxMessage() {
        UserSubscription subscription = new UserSubscription(
                "test-user",
                SubscriptionType.PAID,
                Instant.now().minusSeconds(60)
        );

        userSubscriptionRepository.saveAndFlush(subscription);

        subscriptionService.downgradeExpiredSubscriptions();

        UserSubscription updated = userSubscriptionRepository
                .findById("test-user")
                .orElseThrow();

        SubscriptionLimitResponse limit =
                subscriptionService.getLimit("test-user");

        assertThat(updated.getSubscriptionType())
                .isEqualTo(SubscriptionType.FREE);
        assertThat(updated.getExpiresAt()).isNull();
        assertThat(outboxMessageRepository.count()).isEqualTo(1);
        assertThat(limit.maxFileSizeBytes()).isEqualTo(104_857_600L);
    }
}