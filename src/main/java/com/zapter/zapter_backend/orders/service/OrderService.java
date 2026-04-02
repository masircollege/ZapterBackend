package com.zapter.zapter_backend.orders.service;

import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.orders.dto.OrderOverview;
import com.zapter.zapter_backend.orders.dto.OrderProductDto;
import com.zapter.zapter_backend.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

}
