package com.example.subscription_service.controller;

import com.example.subscription_service.dto.SubscriptionLimitResponse;
import com.example.subscription_service.service.SubscriptionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/{userLogin}/limit")
    public SubscriptionLimitResponse getLimit(
            @PathVariable @NotBlank String userLogin
    ) {
        return subscriptionService.getLimit(userLogin);
    }
}