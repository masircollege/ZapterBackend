package com.zapter.zapter_backend.payment.enums;

public enum PaymentStatus {
    PENDING,    // COD orders — payment not yet received
    SUCCESS,    // Online payment verified
    FAILED      // Razorpay verification failed
}