package com.CardMaster.modules.tap.exception;
public class InsufficientLimitException extends RuntimeException {
    public InsufficientLimitException(Double amount) {
        super("Insufficient available limit for transaction amount: " + amount);
    }

}
