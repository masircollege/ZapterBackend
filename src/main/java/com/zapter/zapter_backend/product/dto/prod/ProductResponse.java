package com.zapter.zapter_backend.product.dto.prod;

import com.zapter.zapter_backend.product.enums.StockStatus;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        Long category,
        Long brand,
        Long model,
        String color,
        String description,
        BigDecimal price,
        StockStatus stockStatus
) {
}
