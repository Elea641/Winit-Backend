package com.cda.winit.auth.infrastructure.exception;

public class PasswordForgottenErrorException extends RuntimeException {

    public PasswordForgottenErrorException(String message) {
        super(message);
    }
}
