package com.zapter.zapter_backend.product.dto.inventory;

import com.zapter.zapter_backend.product.enums.StockStatus;

public record InventoryResponse(
        Long inventoryId,
        Integer quantity,
        Integer minimumCount,
        StockStatus stockStatus
) {}
