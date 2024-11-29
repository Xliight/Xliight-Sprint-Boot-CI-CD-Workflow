package com.ouitrips.app.Exception;

import com.ouitrips.app.exceptions.agency.AgencyNotFoundException;
import com.ouitrips.app.exceptions.agency.CircuitNotFoundException;
import com.ouitrips.app.exceptions.agency.CircuitNotLinkedToAgencyException;
import com.ouitrips.app.exceptions.agency.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestException {


    private GlobalExceptionHandler globalExceptionHandler;

    private AgencyNotFoundException agencyNotFoundException;
    private CircuitNotLinkedToAgencyException circuitNotLinkedToAgencyException;
    private CircuitNotFoundException circuitNotFoundException;

    @BeforeEach
    public void setup() {
        globalExceptionHandler = new GlobalExceptionHandler();

        agencyNotFoundException = Mockito.mock(AgencyNotFoundException.class);
        circuitNotLinkedToAgencyException = Mockito.mock(CircuitNotLinkedToAgencyException.class);
        circuitNotFoundException = Mockito.mock(CircuitNotFoundException.class);

        // Mock the exception messages
        Mockito.when(agencyNotFoundException.getMessage()).thenReturn("Agency not found");
        Mockito.when(circuitNotLinkedToAgencyException.getMessage()).thenReturn("Circuit not linked to agency");
        Mockito.when(circuitNotFoundException.getMessage()).thenReturn("Circuit not found");
    }

    @Test
    public void testHandleAgencyNotFoundException() {
        // Call the exception handler
        ResponseEntity<String> response = globalExceptionHandler.handleAgencyNotFoundException(agencyNotFoundException, null);

        // Assertions to check the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Agency not found", response.getBody());
    }

    @Test
    public void testHandleCircuitNotLinkedToAgencyException() {
        // Call the exception handler
        ResponseEntity<String> response = globalExceptionHandler.handleCircuitNotLinkedToAgency(circuitNotLinkedToAgencyException);

        // Assertions to check the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Circuit not linked to agency", response.getBody());
    }

    @Test
    public void testHandleCircuitNotFoundException() {
        // Call the exception handler
        ResponseEntity<String> response = globalExceptionHandler.handleCircuitNotFound(circuitNotFoundException);

        // Assertions to check the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Circuit not found", response.getBody());
    }
}
