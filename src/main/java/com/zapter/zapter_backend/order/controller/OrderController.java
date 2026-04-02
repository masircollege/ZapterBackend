package com.zapter.zapter_backend.order.controller;

import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.order.domain.Order;
import com.zapter.zapter_backend.order.dto.NewOrderDto;
import com.zapter.zapter_backend.order.dto.OrderOverview;
import com.zapter.zapter_backend.order.dto.OrderProductDto;
import com.zapter.zapter_backend.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ){
        this.orderService = orderService;
    }

    @PostMapping("/overview")
    public ResponseEntity<OrderOverview> getOverview(@RequestBody CartResponse cartResponse){
        try {
           return new ResponseEntity<>(orderService.getOverview(cartResponse),HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> createOrder(@RequestBody NewOrderDto order) {
        try {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
