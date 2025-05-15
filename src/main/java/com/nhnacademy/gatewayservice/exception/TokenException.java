package com.nhnacademy.gatewayservice.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
