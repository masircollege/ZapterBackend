package com.zapter.zapter_backend.cart.dto;

import java.math.BigDecimal;

public interface CartProductDetails {

    Long getProductId();
    String getProductName();
    String getColor();
    BigDecimal getPrice();
    String getStockStatus();
    Integer getQuantity();

}
