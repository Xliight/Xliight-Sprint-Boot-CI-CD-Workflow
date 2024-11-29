package com.ouitrips.app.exceptions.agency;

public class CircuitNotFoundException extends RuntimeException {
    public CircuitNotFoundException(String message) {
        super(message);
    }
}