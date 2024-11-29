package com.ouitrips.app.services.agency;

import com.ouitrips.app.dtos.agencydto.AgencyDTO;
import com.ouitrips.app.dtos.circuitdto.CircuitDTO;
import com.ouitrips.app.entities.agency.Agency;
import com.ouitrips.app.entities.circuits.Circuit;
import java.util.List;

public interface IAgencyService {
    AgencyDTO createAgency(Agency agencyDTO);
    AgencyDTO updateAgency(Integer id, Agency agencyDTO);
    void deleteAgency(Integer id);
    AgencyDTO getAgencyById(Integer id);
    List<AgencyDTO> getAllAgencies();
    // Create a new agency
    CircuitDTO createCircuitForAgency(Circuit circuit);
    List<CircuitDTO> getAllCircuits();
    CircuitDTO getCircuitById(Integer id);
    public CircuitDTO updateCircuit(Integer circuitId, Circuit updatedCircuit);
    void deleteCircuit(Integer id);
}
