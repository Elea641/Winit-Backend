package com.cda.winit.team.insfrastructure.exception;

public class ListTeamByUserAlreadyExistsException extends RuntimeException{
    public ListTeamByUserAlreadyExistsException(String message) {
        super(message);
    }
}