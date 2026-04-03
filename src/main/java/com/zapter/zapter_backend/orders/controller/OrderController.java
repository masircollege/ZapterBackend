package com.zapter.zapter_backend.orders.controller;

import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.orders.dto.NewOrderDto;
import com.zapter.zapter_backend.orders.dto.OrderOverview;
import com.zapter.zapter_backend.orders.dto.OrderSummaryResponse;
import com.zapter.zapter_backend.orders.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/orders")
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

    public ResponseEntity<?> createOrder(@RequestBody NewOrderDto orders) {
        try {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{orderId}/summary")
    public ResponseEntity<OrderSummaryResponse> getOrderSummary(@PathVariable Long orderId) {
        try {
            return new ResponseEntity<>(orderService.getOrderSummary(orderId), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
