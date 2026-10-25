package com.example.subscription_service.scheduler;

import com.example.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionExpirationScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(fixedDelayString = "${app.scheduler.fixed-delay}")
    @SchedulerLock(
            name = "subscription-expiration-scheduler",
            lockAtLeastFor = "PT1S",
            lockAtMostFor = "PT30S"
    )
    public void downgradeExpiredSubscriptions() {
        subscriptionService.downgradeExpiredSubscriptions();
    }
}