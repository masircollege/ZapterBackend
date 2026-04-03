package com.zapter.zapter_backend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CODOrderRequest(
        @NotNull Long cartId,
        @NotNull Long userId,
        @NotBlank String deliveryAddress
) {}