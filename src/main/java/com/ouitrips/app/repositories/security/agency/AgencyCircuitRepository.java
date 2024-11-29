package com.ouitrips.app.repositories.security.agency;

import com.ouitrips.app.entities.agency.Agency;
import com.ouitrips.app.entities.agency.AgencyCircuit;
import com.ouitrips.app.entities.circuits.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyCircuitRepository extends JpaRepository<AgencyCircuit, Integer> {
    boolean existsByAgencyAndCircuit(Agency agency, Circuit circuit);
    Optional<AgencyCircuit> findByAgencyAndCircuit(Agency agency, Circuit circuit);


}
