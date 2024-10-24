package com.cda.winit.tournament.infrastructure.exception;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(Long id) {
        super("Could not find tournament with id " + id);
    }
}