package com.CardMaster.modules.bsp.exception;


public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String reason) {
        super("Payment failed: " + reason);
    }
}
