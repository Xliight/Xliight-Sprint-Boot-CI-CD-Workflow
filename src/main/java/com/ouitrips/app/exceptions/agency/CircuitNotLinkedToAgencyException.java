package com.ouitrips.app.exceptions.agency;

public class CircuitNotLinkedToAgencyException extends RuntimeException {
    public CircuitNotLinkedToAgencyException(Integer circuitId, Integer agencyId) {
        super("Circuit with ID " + circuitId + " does not belong to agency with ID " + agencyId);
    }
}
