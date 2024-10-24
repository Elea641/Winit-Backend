package com.cda.winit.tournament.infrastructure.exception;

public class TournamentServiceException extends RuntimeException {
    public TournamentServiceException(String message) {
        super(message);
    }
}