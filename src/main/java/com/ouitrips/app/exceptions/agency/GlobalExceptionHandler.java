package com.ouitrips.app.exceptions.agency;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgencyNotFoundException.class)
    public ResponseEntity<String> handleAgencyNotFoundException(AgencyNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CircuitNotLinkedToAgencyException.class)
    public ResponseEntity<String> handleCircuitNotLinkedToAgency(CircuitNotLinkedToAgencyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(CircuitNotFoundException.class)
    public ResponseEntity<String> handleCircuitNotFound(CircuitNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    // You can add more exception handlers here for other types of exceptions
}