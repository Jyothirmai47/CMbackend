package com.CardMaster.modules.iam.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid credentials provided");
    }
}
