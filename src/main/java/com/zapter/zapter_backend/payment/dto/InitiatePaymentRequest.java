package com.zapter.zapter_backend.payment.dto;

import com.zapter.zapter_backend.payment.enums.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InitiatePaymentRequest(
        @NotNull Long cartId,
        @NotNull Long userId,
        @NotBlank String deliveryAddress,
        @NotNull PaymentMode paymentMode
) {}