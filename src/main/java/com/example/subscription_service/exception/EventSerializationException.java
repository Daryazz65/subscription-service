package com.example.subscription_service.exception;

public class EventSerializationException extends SubscriptionException {

    public EventSerializationException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}