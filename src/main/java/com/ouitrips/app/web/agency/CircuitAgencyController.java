package com.ouitrips.app.web.agency;

import com.ouitrips.app.dtos.circuitdto.CircuitDTO;
import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.services.agency.impl.AgencyServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("${REST_NAME}/agenciescircuit")
public class CircuitAgencyController {

    private final AgencyServiceImpl agencyService; // Assuming you have a service to handle the business logic



    @PostMapping
    public ResponseEntity<CircuitDTO> createCircuit(@RequestBody Circuit circuit) {
        try {
            CircuitDTO createdCircuit = agencyService.createCircuitForAgency(circuit);
            return new ResponseEntity<>(createdCircuit, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<List<CircuitDTO>> getAllCircuits() {
        List<CircuitDTO> circuits = agencyService.getAllCircuits();
        return ResponseEntity.ok(circuits);
    }

    // Endpoint to get a circuit by ID
    @GetMapping("/{id}")
    public ResponseEntity<CircuitDTO> getCircuitById(@PathVariable Integer id) {
        CircuitDTO circuit = agencyService.getCircuitById(id);
        return circuit != null ? ResponseEntity.ok(circuit) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CircuitDTO> updateCircuit(@PathVariable Integer id, @RequestBody Circuit updatedCircuit) {
        CircuitDTO circuitDTO = agencyService.updateCircuit(id, updatedCircuit);
        return ResponseEntity.ok(circuitDTO);
    }

    // Delete circuit association
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuit(@PathVariable Integer id) {
        agencyService.deleteCircuit(id);
        return ResponseEntity.noContent().build();
    }

}