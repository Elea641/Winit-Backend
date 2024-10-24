package com.cda.winit.auth.infrastructure.exception;

public class PasswordTokenException extends RuntimeException{
    public PasswordTokenException() {
        super("Le token n'est pas valide");
    }
}
