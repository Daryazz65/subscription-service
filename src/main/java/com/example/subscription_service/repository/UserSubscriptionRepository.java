package com.example.subscription_service.repository;

import com.example.subscription_service.domain.SubscriptionType;
import com.example.subscription_service.domain.UserSubscription;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSubscriptionRepository
        extends JpaRepository<UserSubscription, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select subscription
            from UserSubscription subscription
            where subscription.subscriptionType = :subscriptionType
              and subscription.expiresAt <= :currentTime
            """)
    List<UserSubscription> findExpiredForUpdate(
            @Param("subscriptionType") SubscriptionType subscriptionType,
            @Param("currentTime") Instant currentTime
    );
}