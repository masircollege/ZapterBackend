package com.zapter.zapter_backend.order.service;

import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.order.dto.OrderOverview;
import com.zapter.zapter_backend.order.dto.OrderProductDto;
import com.zapter.zapter_backend.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderOverview getOverview(CartResponse cartResponse) {
        try {
            LocalDateTime dispatchDate = LocalDateTime.now();
            LocalDateTime deliveryDate = LocalDateTime.now();
            dispatchDate = dispatchDate.plusDays(1);
            deliveryDate = deliveryDate.plusDays(4);

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
                    deliveryDate,
                    dispatchDate
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
