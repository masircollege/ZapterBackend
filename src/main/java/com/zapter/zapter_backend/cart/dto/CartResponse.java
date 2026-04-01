package com.zapter.zapter_backend.cart.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CartResponse(
		Long id,
		Long userId,
		Set<CartProductDto> products,
		BigDecimal totalPrice
		) {}
