package com.zapter.zapter_backend.product.dto.inventory;

public record InventoryResponse(
        Long id,
        Integer quantity,
        Integer minimumCount,
        Long productId,
        Long warehouseId,
        Long vendorId
//        StockStatus stockStatus,
) {}
