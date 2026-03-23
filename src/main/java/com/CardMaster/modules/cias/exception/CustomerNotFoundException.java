package com.CardMaster.modules.cias.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {

        super("Customer not found with ID: " + id);
    }
}
