package com.example.subscription_service.exception;

public class SubscriptionNotFoundException extends SubscriptionException {

    public SubscriptionNotFoundException(String userLogin) {
        super("Subscription not found for user: " + userLogin);
    }
}