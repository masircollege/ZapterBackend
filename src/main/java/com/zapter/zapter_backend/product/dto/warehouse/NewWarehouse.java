package com.zapter.zapter_backend.product.dto.warehouse;

public record NewWarehouse(
        String name,
        String address,
        Long vendorId
) {}
