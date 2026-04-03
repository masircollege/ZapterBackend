package com.zapter.zapter_backend.orders.service;

import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.orders.domain.Order;
import com.zapter.zapter_backend.orders.dto.OrderOverview;
import com.zapter.zapter_backend.orders.dto.OrderProductDto;
import com.zapter.zapter_backend.orders.dto.OrderSummaryResponse;
import com.zapter.zapter_backend.orders.repository.OrderRepository;
import com.zapter.zapter_backend.payment.domain.Payment;
import com.zapter.zapter_backend.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository
    ) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public OrderOverview getOverview(CartResponse cartResponse) {
        try {
            final LocalDateTime dispatchDate = LocalDateTime.now().plusDays(1);
            final LocalDateTime deliveryDate = LocalDateTime.now().plusDays(4);

            return new OrderOverview(
                    cartResponse.userId(),
                    cartResponse.id(),
                    cartResponse.products().stream().
                            map(product -> new OrderProductDto(
                                            product.getProductId(),
                                            product.getProductName(),
                                            product.getColor(),
                                            product.getPrice(),
                                            product.getQuantity()
                                    )
                            ).collect(Collectors.toSet()),
                    cartResponse.totalPrice(),
                    dispatchDate,
                    deliveryDate
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OrderSummaryResponse getOrderSummary(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        Payment payment = order.getPayment(); // via OneToOne

        Set<OrderProductDto> productDtos = order.getOrderProduct().stream()
                .map(op -> new OrderProductDto(
                        op.getOrderProduct().getId(),
                        op.getOrderProduct().getName(),
                        op.getOrderProduct().getColor(),
                        op.getOrderProduct().getPrice(),
                        op.getQuantity()
                ))
                .collect(Collectors.toSet());

        return new OrderSummaryResponse(
                order.getId(),
                productDtos,
                order.getAmount(),
                order.getAddress(),
                order.getDispatchDate(),
                order.getDeliveryDate(),
                order.getStatus(),
                payment.getPaymentMode(),
                payment.getPaymentStatus(),
                payment.getRazorpayPaymentId() // null for COD — frontend handles this gracefully
        );

    }
}