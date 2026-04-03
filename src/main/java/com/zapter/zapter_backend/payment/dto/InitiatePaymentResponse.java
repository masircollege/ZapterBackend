package com.zapter.zapter_backend.payment.dto;

import java.math.BigDecimal;

public record InitiatePaymentResponse(
        String razorpayOrderId,
        BigDecimal amount,          // in rupees — frontend multiplies by 100 for Razorpay
        String currency,
        String keyId                // safe to expose: this is the PUBLIC key
) {}