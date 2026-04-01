package com.zapter.zapter_backend.product.dto.prod;

import com.zapter.zapter_backend.product.dto.KeyFeaturesDTO;
import com.zapter.zapter_backend.product.enums.StockStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        Long category,
        Long brand,
        Long model,
        String color,
        List<KeyFeaturesDTO> keyFeatures,
        String description,
        BigDecimal price,
        StockStatus stockStatus
) {
}
