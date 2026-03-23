package com.CardMaster.modules.bsp.exception;


public class StatementNotFoundException extends RuntimeException {
    public StatementNotFoundException(Long id) {
        super("Statement not found with id: " + id);
    }
}