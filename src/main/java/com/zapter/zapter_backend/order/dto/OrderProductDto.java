package com.zapter.zapter_backend.order.dto;

import java.math.BigDecimal;

public record OrderProductDto(
        Long productId,
        String name,
        String color,
        BigDecimal price,
        Integer quantity
) {
}
