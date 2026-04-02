package com.zapter.zapter_backend.orders.dto;

import com.zapter.zapter_backend.product.domain.Product;
import com.zapter.zapter_backend.orders.enums.Status;

import java.math.BigDecimal;
import java.util.Set;

public record OrderResponse(
		Long orderId,
		Set<Product> product,
		Status status,
		BigDecimal amount
		) {}
