package com.zapter.zapter_backend.cart.controller;

import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }

//    @PreAuthorize("hasAuthority('USER')")

    @PostMapping("/{user_id},{product_id}")
    public ResponseEntity<?> addToCart(
            @PathVariable("user_id") Long userId,
            @PathVariable("product_id") Long productId
    ){
        try {
            cartService.addToCart(userId,productId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable("user_id") Long userId) {
        try {
            return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
