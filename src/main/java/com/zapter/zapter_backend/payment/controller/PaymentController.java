package com.zapter.zapter_backend.payment.controller;

import com.zapter.zapter_backend.payment.dto.*;
import com.zapter.zapter_backend.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Online flow Step 1: frontend calls this after user clicks "PAY NOW".
     * Returns the Razorpay Order ID + public key for Checkout.js.
     */
    @PostMapping("/initiate")
    public ResponseEntity<InitiatePaymentResponse> initiatePayment(
            @Valid @RequestBody InitiatePaymentRequest request
    ) {
        return new ResponseEntity<>(
                paymentService.initiateOnlinePayment(request),
                HttpStatus.OK
        );
    }

    /**
     * Online flow Step 2: frontend calls this after Razorpay Checkout succeeds.
     * Verifies signature, creates Order in DB, updates everything.
     */
    @PostMapping("/verify")
    public ResponseEntity<PaymentOrderCreatedResponse> verifyAndCreate(
            @Valid @RequestBody VerifyPaymentRequest request
    ) {
        return new ResponseEntity<>(
                paymentService.verifyAndCreateOrder(request),
                HttpStatus.CREATED
        );
    }

    /**
     * COD flow: single call, creates order directly.
     */
    @PostMapping("/cod")
    public ResponseEntity<PaymentOrderCreatedResponse> codOrder(
            @Valid @RequestBody CODOrderRequest request
    ) {
        return new ResponseEntity<>(
                paymentService.createCODOrder(request),
                HttpStatus.CREATED
        );
    }
}