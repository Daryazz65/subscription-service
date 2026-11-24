package com.example.subscription_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Subscription subscription,
        Scheduler scheduler,
        Outbox outbox,
        Topics topics
) {

    public record Subscription(
            long freeMaxFileSizeBytes
    ) {
    }

    public record Scheduler(
            long fixedDelay
    ) {
    }

    public record Outbox(
            long fixedDelay,
            int batchSize
    ) {
    }

    public record Topics(
            String subscriptionChanged
    ) {
    }
}