package com.CardMaster.modules.cpl.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
    public BadRequestException(String message, Throwable cause) { super(message, cause); }
}