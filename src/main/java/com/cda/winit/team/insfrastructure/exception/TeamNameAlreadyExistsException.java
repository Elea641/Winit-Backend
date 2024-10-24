package com.cda.winit.team.insfrastructure.exception;

public class TeamNameAlreadyExistsException extends RuntimeException {
    public TeamNameAlreadyExistsException(String message) {
        super(message);
    }
}