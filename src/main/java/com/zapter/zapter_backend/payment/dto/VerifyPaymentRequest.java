package com.zapter.zapter_backend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyPaymentRequest(
        @NotBlank String razorpayOrderId,
        @NotBlank String razorpayPaymentId,
        @NotBlank String razorpaySignature,
        @NotNull Long cartId,
        @NotNull Long userId,
        @NotBlank String deliveryAddress
) {}