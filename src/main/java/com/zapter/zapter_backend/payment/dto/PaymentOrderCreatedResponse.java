package com.zapter.zapter_backend.payment.dto;

public record PaymentOrderCreatedResponse(
        Long orderId,
        String message
) {}