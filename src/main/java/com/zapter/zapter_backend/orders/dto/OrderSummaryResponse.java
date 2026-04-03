package com.zapter.zapter_backend.orders.dto;

import com.zapter.zapter_backend.payment.enums.PaymentMode;
import com.zapter.zapter_backend.payment.enums.PaymentStatus;
import com.zapter.zapter_backend.orders.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderSummaryResponse(
        Long orderId,
        // Product details
        Set<OrderProductDto> products,
        BigDecimal totalAmount,
        // Delivery details
        String deliveryAddress,
        LocalDateTime dispatchDate,
        LocalDateTime deliveryDate,
        // Order status
        Status orderStatus,
        // Payment details
        PaymentMode paymentMode,
        PaymentStatus paymentStatus,
        String razorpayPaymentId    // null for COD
) {}