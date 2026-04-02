package com.zapter.zapter_backend.cart.dto;

import com.zapter.zapter_backend.cart.dto.interfaces.CartProductDetails;

import java.math.BigDecimal;
import java.util.Set;

public record CartResponse(
		Long id,
		Long userId,
		Set<CartProductDetailsDto> products,
		BigDecimal totalPrice
		) {}
