package com.example.subscription_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_subscription")
public class UserSubscription {

    @Id
    @Column(name = "user_login")
    private String userLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    private SubscriptionType subscriptionType;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected UserSubscription() {
    }

    public UserSubscription(
            String userLogin,
            SubscriptionType subscriptionType,
            Instant expiresAt
    ) {
        this.userLogin = userLogin;
        this.subscriptionType = subscriptionType;
        this.expiresAt = expiresAt;
    }

    public boolean hasActivePaidSubscription(Instant currentTime) {
        return subscriptionType == SubscriptionType.PAID
                && expiresAt != null
                && expiresAt.isAfter(currentTime);
    }

    public void downgradeToFree() {
        subscriptionType = SubscriptionType.FREE;
        expiresAt = null;
        updatedAt = Instant.now();
    }
}