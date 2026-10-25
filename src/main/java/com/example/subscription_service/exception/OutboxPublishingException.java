package com.example.subscription_service.exception;

public class OutboxPublishingException extends SubscriptionException {

    public OutboxPublishingException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}