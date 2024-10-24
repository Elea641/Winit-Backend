package com.cda.winit.auth.infrastructure.exception;

public class PasswordNotSimilarException extends RuntimeException{
    public PasswordNotSimilarException() {
        super("Les mots de passe doivent être identiques");
    }
}
