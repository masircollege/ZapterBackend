package com.zapter.zapter_backend.cart.dto;

import com.zapter.zapter_backend.cart.dto.interfaces.CartProductDetails;

import java.math.BigDecimal;

public record CartProductDetailsDto(
        Long productId,
        String productName,
        String color,
        BigDecimal price,
        Integer quantity,
        String stockStatus

) implements CartProductDetails {

    @Override
    public Long getProductId() {
        return productId;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String getStockStatus() {
        return stockStatus;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }
}
