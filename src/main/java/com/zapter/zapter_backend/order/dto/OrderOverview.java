package com.zapter.zapter_backend.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderOverview(
        Long userId,
        Long cartId,
        Set<OrderProductDto> orderProducts,
        BigDecimal totalAmount,
        LocalDateTime dispatchDate,
        LocalDateTime deliveryDate
) {
}
