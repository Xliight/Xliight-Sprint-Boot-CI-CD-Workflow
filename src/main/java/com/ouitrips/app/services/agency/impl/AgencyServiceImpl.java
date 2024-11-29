package com.ouitrips.app.services.agency.impl;

import com.ouitrips.app.dtos.AgencyMapper;
import com.ouitrips.app.dtos.agencydto.AgencyDTO;
import com.ouitrips.app.dtos.circuitdto.CircuitDTO;
import com.ouitrips.app.entities.agency.Agency;
import com.ouitrips.app.entities.agency.AgencyCircuit;
import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.agency.AgencyException;
import com.ouitrips.app.exceptions.agency.CircuitNotFoundException;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.repositories.security.agency.AgencyCircuitRepository;
import com.ouitrips.app.repositories.security.agency.AgencyRepository;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.services.agency.IAgencyService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ouitrips.app.dtos.CircuitMapper;
import java.util.List;

@AllArgsConstructor
@Service
public class AgencyServiceImpl implements IAgencyService {

    private final AgencyRepository agencyRepository;
    private final CircuitRepository circuitRepository;
    private final AgencyCircuitRepository agencyCircuitRepository;
    private final UserRepository userRepository;
    private static final String AGENCY_ERROR = "Agency with that ID already exists ";
    private static final String CIRCUIT_ERROR = "Circuit with that ID dont exist ";// Compliant
    private static final AgencyMapper agencyMapper = AgencyMapper.INSTANCE;
    private static final CircuitMapper circuitMapper = CircuitMapper.INSTANCE;

    @Override
    public AgencyDTO createAgency(Agency agencybody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User owner = userRepository.findByUsername(username); // Assuming findByUsername returns User directly

        // Check if the owner already has an agency
        if (agencyRepository.existsByOwnerId(owner.getId())) {
            throw new IllegalArgumentException("Owner already has an agency.");
        }

        Agency agency = agencybody;
        agency.setOwner(owner);
        agency.setIsDeleted(false); // Set to false by default when creating

        return agencyMapper.agencyToAgencyDTO(agencyRepository.save(agency));
    }

    @Override
    public AgencyDTO updateAgency(Integer id, Agency agencyDTO) {
        // Fetch the existing agency by ID
        Agency existingAgency = agencyRepository.findById(id)
                .orElseThrow(() -> new AgencyException("Agency not found"));

        // Check if the agency is marked as deleted
        if (Boolean.TRUE.equals(existingAgency.getIsDeleted())) {
            throw new AgencyException("Cannot update a deleted agency.");
        }

        // Update agency details
        existingAgency.setName(agencyDTO.getName());
        existingAgency.setAddress(agencyDTO.getAddress());
        existingAgency.setContactNumber(agencyDTO.getContactNumber());
        existingAgency.setEmail(agencyDTO.getEmail());
        existingAgency.setDescription(agencyDTO.getDescription());

        return agencyMapper.agencyToAgencyDTO(agencyRepository.save(existingAgency));
    }


    @Override
    public void deleteAgency(Integer id) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new AgencyException("Agency not found"));

        agency.setIsDeleted(true); // Soft delete
        agencyRepository.save(agency);
    }
    @Override
    public AgencyDTO getAgencyById(Integer id) {
        Agency agency = agencyRepository.findById(id)
                .filter(ag -> !ag.getIsDeleted()) // Ensure agency is not deleted
                .orElseThrow(() -> new AgencyException("Agency not found or has been deleted"));

        return agencyMapper.agencyToAgencyDTO(agency);
    }

    @Override
    public List<AgencyDTO> getAllAgencies() {
        return agencyRepository.findAllByIsDeletedFalse()
                .stream()
                .map(agencyMapper::agencyToAgencyDTO)
                .toList();
    }

    @Override
    @Transactional
    public CircuitDTO createCircuitForAgency(Circuit circuit) {
        // Get the currently connected user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the user from the database
        User connectedUser = userRepository.findByUsername(username);

        // Find the agency owned by this user
        Agency agency = agencyRepository.findByOwner(connectedUser);

        // Check if the agency is not deleted
        if (Boolean.TRUE.equals(agency.getIsDeleted())) {
            throw new CircuitNotFoundException("Cannot create a circuit for a deleted agency");
        }

        // Set the circuit's isDeleted to false
        circuit.setIsDeleted(false);

        // Save the circuit
        Circuit savedCircuit = circuitRepository.save(circuit);

        // Create and save the agency-circuit association
        AgencyCircuit agencyCircuit = new AgencyCircuit();
        agencyCircuit.setAgency(agency);
        agencyCircuit.setCircuit(savedCircuit);
        agencyCircuit.setDescription("Created circuit for agency"); // Set a description as needed
        agencyCircuitRepository.save(agencyCircuit);

        // Add the saved AgencyCircuit to the circuit's agencyCircuits list
        savedCircuit.getAgencyCircuits().add(agencyCircuit);
        // Map the saved circuit to CircuitDTO using CircuitMapper
        return circuitMapper.toCircuitDTO(savedCircuit);
    }
    @Override
    public List<CircuitDTO> getAllCircuits() {
        return circuitRepository.findAll().stream()
                .map(CircuitMapper.INSTANCE::toCircuitDTO) // Map Circuit to CircuitDTO
                .toList();
    }

    // Fetch circuit by ID
    @Override
    public CircuitDTO getCircuitById(Integer id) {
        // Fetch the circuit by ID
        Circuit circuit = circuitRepository.findById(id)
                .orElseThrow(() -> new CircuitNotFoundException("Circuit with ID " + id + " not found"));

        // Map the circuit to CircuitDTO and return
        return CircuitMapper.INSTANCE.toCircuitDTO(circuit);
    }
    @Override
    @Transactional
    public CircuitDTO updateCircuit(Integer circuitId, Circuit updatedCircuit) {
        // Fetch the existing circuit
        Circuit existingCircuit = circuitRepository.findById(circuitId)
                .orElseThrow(() -> new CircuitNotFoundException("Circuit not found"));

        // Fetch the currently connected user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the user from the database
        User connectedUser = userRepository.findByUsername(username);

        // Find the agency owned by this user
        Agency agency = agencyRepository.findByOwner(connectedUser);

        // Check if the circuit is linked to the same agency
        boolean isLinkedToAgency = agencyCircuitRepository.existsByAgencyAndCircuit(agency, existingCircuit);

        if (!isLinkedToAgency) {
            throw new CircuitNotFoundException("Circuit is not linked to this agency");
        }

        // Update the properties of the existing circuit with values from updatedCircuit
        existingCircuit.setName(updatedCircuit.getName()); // Assuming Circuit has a name property
        existingCircuit.setDistance(updatedCircuit.getDistance());
        existingCircuit.setDateDebut(updatedCircuit.getDateDebut());
        existingCircuit.setDateFin(updatedCircuit.getDateFin());
        existingCircuit.setSteps(updatedCircuit.getSteps());
        existingCircuit.setCircuitGroup(updatedCircuit.getCircuitGroup());

        // Add more fields to update as necessary

        // Save the updated circuit
        Circuit savedCircuit = circuitRepository.save(existingCircuit);

        // Return the updated CircuitDTO
        return CircuitMapper.INSTANCE.toCircuitDTO(savedCircuit);
    }
    @Override
    @Transactional
    public void deleteCircuit(Integer circuitId) {
        // Fetch the currently connected user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the user from the database
        User connectedUser = userRepository.findByUsername(username);

        // Find the agency owned by this user
        Agency agency = agencyRepository.findByOwner(connectedUser);

        // Find the existing circuit
        Circuit existingCircuit = circuitRepository.findById(circuitId)
                .orElseThrow(() -> new CircuitNotFoundException("Circuit not found"));

        // Fetch the AgencyCircuit relation to be deleted
        AgencyCircuit agencyCircuit = agencyCircuitRepository.findByAgencyAndCircuit(agency, existingCircuit)
                .orElseThrow(() -> new CircuitNotFoundException("No relation exists between the agency and the circuit"));

        // Remove the relationship from both sides
        agency.getAgencyCircuits().remove(agencyCircuit);
        existingCircuit.getAgencyCircuits().remove(agencyCircuit);

        // Delete the relation
        agencyCircuitRepository.deleteById(agencyCircuit.getId());
    }

}