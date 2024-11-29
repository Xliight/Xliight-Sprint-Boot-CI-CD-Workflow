package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.TrackingCircuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingCircuitRepository extends JpaRepository<TrackingCircuit, Integer>, JpaSpecificationExecutor<TrackingCircuit> {
    List<TrackingCircuit> findByCircuitIdOrderByStartDate(Integer circuitReference);
}
