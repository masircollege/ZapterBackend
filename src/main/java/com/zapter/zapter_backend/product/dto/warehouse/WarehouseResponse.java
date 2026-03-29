package com.zapter.zapter_backend.product.dto.warehouse;

public record WarehouseResponse(
        Long id,
        String name,
        String address,
        Long vendorId
) {}
