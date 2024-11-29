package com.ouitrips.app.services.agency.impl;

import com.ouitrips.app.dtos.AgencyMapper;
import com.ouitrips.app.dtos.CircuitMapper;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgencyServiceImplTest {
    @Mock
    private AgencyRepository agencyRepository;

    @Mock
    private CircuitRepository circuitRepository;

    @Mock
    private AgencyCircuitRepository agencyCircuitRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AgencyMapper agencyMapper;

    @Mock
    private CircuitMapper circuitMapper;

    @Mock
    private Authentication authentication;
    @InjectMocks
    private AgencyServiceImpl agencyService;

    @Mock
    private SecurityContext securityContext;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

    }


    @Test
    void testCreateAgencySuccess() {
        // Arrange: Set up the authentication context and mock objects
        String username = "testUser";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);

        User owner = new User();
        owner.setId(1);
        when(userRepository.findByUsername(username)).thenReturn(owner);
        when(agencyRepository.existsByOwnerId(owner.getId())).thenReturn(false);

        Agency agency = new Agency();
        agency.setId(1);
        agency.setName("testAgency");
        agency.setOwner(owner);
        agency.setIsDeleted(false);

        AgencyDTO expectedAgencyDTO = new AgencyDTO();
        expectedAgencyDTO.setId(1);
        expectedAgencyDTO.setName("testAgency");
        expectedAgencyDTO.setOwnerId(owner.getId());
        when(agencyRepository.save(any(Agency.class))).thenReturn(agency);
        when(agencyMapper.agencyToAgencyDTO(agency)).thenReturn(expectedAgencyDTO);

        // Act: Call the createAgency method
        AgencyDTO result = agencyService.createAgency(agency);

        assertEquals(expectedAgencyDTO.getId(), result.getId());
        assertEquals(expectedAgencyDTO.getName(), result.getName());
        assertEquals(expectedAgencyDTO.getOwnerId(), result.getOwnerId());


    }

    @Test
    void testCreateAgencyThrowsExceptionIfOwnerHasAgency() {
        // Arrange: Set up the authentication context and mock objects
        String username = "testUser";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);

        User owner = new User();
        owner.setId(1);
        when(userRepository.findByUsername(username)).thenReturn(owner);
        when(agencyRepository.existsByOwnerId(owner.getId())).thenReturn(true);

        Agency agency = new Agency();

        // Act & Assert: Call createAgency and expect an IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            agencyService.createAgency(agency);
        });

        // Verify exception message
        assertEquals("Owner already has an agency.", exception.getMessage());

        // Verify that save and agencyToAgencyDTO are not called
        verify(agencyRepository, never()).save(any(Agency.class));
        verify(agencyMapper, never()).agencyToAgencyDTO(any(Agency.class));
    }

    @Test
    void testUpdateAgencySuccess() {
        Agency existingAgency = new Agency();
        existingAgency.setId(1);
        existingAgency.setName("Old Name");
        existingAgency.setIsDeleted(false);

        Agency updatedAgency = new Agency();
        updatedAgency.setId(1);
        updatedAgency.setName("New Name");
        updatedAgency.setAddress("New Address");
        updatedAgency.setContactNumber("123456789");
        updatedAgency.setEmail("new@example.com");
        updatedAgency.setDescription("Updated description");

       AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(1);
        agencyDTO.setName("New Name");
        agencyDTO.setAddress("New Address");
        agencyDTO.setContactNumber("123456789");
        agencyDTO.setEmail("new@example.com");
        agencyDTO.setDescription("Updated description");
        // Arrange
        when(agencyRepository.findById(1)).thenReturn(Optional.of(existingAgency));
        when(agencyRepository.save(any(Agency.class))).thenReturn(updatedAgency);
        when(agencyMapper.agencyToAgencyDTO(updatedAgency)).thenReturn(agencyDTO);

        // Act
        AgencyDTO result = agencyService.updateAgency(1, updatedAgency);

        // Assert
        assertEquals(agencyDTO.getName(), result.getName());
        assertEquals(agencyDTO.getAddress(), result.getAddress());
        assertEquals(agencyDTO.getContactNumber(), result.getContactNumber());
        assertEquals(agencyDTO.getEmail(), result.getEmail());
        assertEquals(agencyDTO.getDescription(), result.getDescription());


    }

    @Test
    void testUpdateAgencyThrowsExceptionIfAgencyNotFound() {
        Agency existingAgency = new Agency();
        existingAgency.setId(1);
        existingAgency.setName("Old Name");
        existingAgency.setIsDeleted(false);

        Agency updatedAgency = new Agency();
        updatedAgency.setId(1);
        updatedAgency.setName("New Name");
        updatedAgency.setAddress("New Address");
        updatedAgency.setContactNumber("123456789");
        updatedAgency.setEmail("new@example.com");
        updatedAgency.setDescription("Updated description");
        // Arrange
        when(agencyRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AgencyException.class, () -> agencyService.updateAgency(1, updatedAgency));
        verify(agencyRepository, times(1)).findById(1);
        verify(agencyRepository, never()).save(any(Agency.class));
        verify(agencyMapper, never()).agencyToAgencyDTO(any(Agency.class));
    }
    @Test
    void testUpdateAgencyThrowsExceptionIfAgencyIsDeleted() {
        Agency existingAgency = new Agency();
        existingAgency.setId(1);
        existingAgency.setName("Old Name");
        existingAgency.setIsDeleted(false);

        Agency updatedAgency = new Agency();
        updatedAgency.setId(1);
        updatedAgency.setName("New Name");
        updatedAgency.setAddress("New Address");
        updatedAgency.setContactNumber("123456789");
        updatedAgency.setEmail("new@example.com");
        updatedAgency.setDescription("Updated description");
        // Arrange
        existingAgency.setIsDeleted(true);
        when(agencyRepository.findById(1)).thenReturn(Optional.of(existingAgency));
        // Act & Assert
        assertThrows(AgencyException.class, () -> agencyService.updateAgency(1, updatedAgency));
        verify(agencyRepository, times(1)).findById(1);
        verify(agencyRepository, never()).save(any(Agency.class));
        verify(agencyMapper, never()).agencyToAgencyDTO(any(Agency.class));
    }


    @Test
    void testDeleteAgencySuccess() {
        Agency  agency = new Agency();
        agency.setId(1);
        agency.setName("Test Agency");
        agency.setIsDeleted(false);
        // Arrange: Mock the findById to return the agency
        when(agencyRepository.findById(1)).thenReturn(Optional.of(agency));

        // Act: Call the deleteAgency method
        agencyService.deleteAgency(1);

        // Assert: Verify that setIsDeleted(true) is called and save is invoked
        verify(agencyRepository, times(1)).findById(1);
        verify(agencyRepository, times(1)).save(agency);
        assertTrue(agency.getIsDeleted(), "Agency should be marked as deleted");
    }

    @Test
    void testDeleteAgencyThrowsExceptionWhenNotFound() {
        // Arrange: Mock the findById to return empty
        when(agencyRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Verify that an AgencyException is thrown
        assertThrows(AgencyException.class, () -> agencyService.deleteAgency(1));

        // Verify that no save was called
        verify(agencyRepository, times(1)).findById(1);
        verify(agencyRepository, never()).save(any(Agency.class));
    }

    @Test
    void testGetAgencyByIdSuccess() {
        Agency agency = new Agency();
        agency.setId(1);
        agency.setName("Test Agency");
        agency.setIsDeleted(false);

        // Setting up a sample AgencyDTO
       AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(1);
        agencyDTO.setName("Test Agency");
        // Arrange: Mock the findById method to return the agency
        when(agencyRepository.findById(1)).thenReturn(Optional.of(agency));
        when(agencyMapper.agencyToAgencyDTO(agency)).thenReturn(agencyDTO);

        // Act: Call the getAgencyById method
        AgencyDTO result = agencyService.getAgencyById(1);

        // Assert: Verify that the agency is mapped to a DTO and returned
        assertNotNull(result, "AgencyDTO should not be null");
        assertEquals(agencyDTO.getName(), result.getName(), "Agency names should match");

    }

    @Test
    void testGetAgencyByIdThrowsExceptionWhenAgencyNotFound() {
        // Arrange: Mock the findById method to return empty (agency not found)
        when(agencyRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Verify that an AgencyException is thrown
        assertThrows(AgencyException.class, () -> agencyService.getAgencyById(1));

        // Verify that no mapping is done
        verify(agencyRepository, times(1)).findById(1);
        verify(agencyMapper, never()).agencyToAgencyDTO(any(Agency.class));
    }

    @Test
    void testGetAgencyByIdThrowsExceptionWhenAgencyIsDeleted() {
        Agency agency = new Agency();
        agency.setId(1);
        agency.setName("Test Agency");
        agency.setIsDeleted(false);

        // Arrange: Mock the findById method to return a deleted agency
        agency.setIsDeleted(true);
        when(agencyRepository.findById(1)).thenReturn(Optional.of(agency));

        // Act & Assert: Verify that an AgencyException is thrown
        assertThrows(AgencyException.class, () -> agencyService.getAgencyById(1));

        // Verify that no mapping is done
        verify(agencyRepository, times(1)).findById(1);
        verify(agencyMapper, never()).agencyToAgencyDTO(any(Agency.class));
    }

    @Test
    void testGetAllAgenciesSuccess() {
       Agency agency = new Agency();
        agency.setId(1);
        agency.setName("Test Agency");
        agency.setIsDeleted(false);

        // Setting up a sample AgencyDTO
       AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(1);
        agencyDTO.setName("Test Agency");
        // Arrange: Mock the findAllByIsDeletedFalse method to return a list with one agency
        when(agencyRepository.findAllByIsDeletedFalse()).thenReturn(List.of(agency));
        when(agencyMapper.agencyToAgencyDTO(agency)).thenReturn(agencyDTO);

        // Act: Call the getAllAgencies method
        List<AgencyDTO> result = agencyService.getAllAgencies();

        // Assert: Verify the returned list has one item and that it matches the expected DTO
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain one agency");
        assertEquals(agencyDTO.getName(), result.get(0).getName(), "Agency names should match");

    }

    @Test
    void testGetAllAgenciesNoAgencies() {
        // Arrange: Mock the findAllByIsDeletedFalse method to return an empty list
        when(agencyRepository.findAllByIsDeletedFalse()).thenReturn(List.of());

        // Act: Call the getAllAgencies method
        List<AgencyDTO> result = agencyService.getAllAgencies();

        // Assert: Verify the returned list is empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty");
        verify(agencyRepository, times(1)).findAllByIsDeletedFalse();
        verify(agencyMapper, never()).agencyToAgencyDTO(any(Agency.class));
    }

    @Test
    void testCreateCircuitForAgency_Success() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Initialize test objects
        User connectedUser = new User();
        connectedUser.setUsername("testUser");

        Agency agency = new Agency();
        agency.setOwner(connectedUser);
        agency.setIsDeleted(false);

        Circuit circuit = new Circuit();
        circuit.setId(1);
        circuit.setIsDeleted(false);

        CircuitDTO circuitDTO = new CircuitDTO();

        when(authentication.getName()).thenReturn("testUser");
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(connectedUser);
        when(agencyRepository.findByOwner(connectedUser)).thenReturn(agency);
        when(circuitRepository.save(circuit)).thenReturn(circuit);
        when(circuitMapper.toCircuitDTO(circuit)).thenReturn(circuitDTO);

        // Act
        CircuitDTO result = agencyService.createCircuitForAgency(circuit);
        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1,result.getId());

    }

    @Test
    void testCreateCircuitForAgency_AgencyDeleted() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Initialize test objects
        User connectedUser = new User();
        connectedUser.setUsername("testUser");

        Agency agency = new Agency();
        agency.setOwner(connectedUser);
        agency.setIsDeleted(true);

        Circuit circuit = new Circuit();
        circuit.setId(1);
        circuit.setIsDeleted(false);

        when(authentication.getName()).thenReturn("testUser");
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(connectedUser);
        when(agencyRepository.findByOwner(connectedUser)).thenReturn(agency);

        CircuitNotFoundException exception2 = assertThrows(
                CircuitNotFoundException.class,
                () ->   agencyService.createCircuitForAgency(circuit)
        );
        Assertions.assertNotNull(exception2);
        Assertions.assertEquals("Cannot create a circuit for a deleted agency", exception2.getMessage());
    }
    @Test
    void testGetAllCircuits() {
        // Arrange: create some Circuit objects and their corresponding DTOs
        Circuit circuit1 = new Circuit();
        circuit1.setId(1);
        circuit1.setName("Test Circuit");
        Circuit circuit2 = new Circuit();
        circuit1.setId(2);
        circuit1.setName("Test Circuit2");

        CircuitDTO circuitDTO1 = new CircuitDTO();
        circuitDTO1.setId(circuit1.getId());
        circuitDTO1.setName(circuit1.getName());
        CircuitDTO circuitDTO2 = new CircuitDTO();
        circuitDTO2.setId(circuit2.getId());
        circuitDTO2.setName(circuit2.getName());

        when(circuitRepository.findAll()).thenReturn(Arrays.asList(circuit1, circuit2));
        List<CircuitDTO> result = agencyService.getAllCircuits();

        assertEquals(2, result.size());
        assertEquals(circuitDTO1.getId(), result.get(0).getId());
        assertEquals(circuitDTO1.getName(), result.get(0).getName());
        assertEquals(circuitDTO2.getId(), result.get(1).getId());
        assertEquals(circuitDTO2.getName(), result.get(1).getName());
    }
    @Test
    void testGetCircuitById_Success() {
        // Arrange
        Integer circuitId = 1;
        Circuit circuit = new Circuit();
        circuit.setId(circuitId);
        circuit.setName("Test Circuit");
        CircuitDTO circuitDTO = new CircuitDTO();
        circuitDTO.setId(circuitId);
        circuitDTO.setName("Test Circuit");

        // Mock repository and mapper behavior
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));

        // Act
        CircuitDTO result = agencyService.getCircuitById(circuitId);

        // Assert
        assertEquals(circuitDTO.getName(), result.getName(), "The returned CircuitDTO should match the expected value");
    }
    @Test
    void testGetCircuitById_NotFound() {
        // Arrange
        Integer circuitId = 2;

        // Mock repository behavior to return an empty result
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CircuitNotFoundException.class, () -> agencyService.getCircuitById(circuitId),
                "Expected CircuitNotFoundException when circuit is not found");
    }
    @Test
    void testUpdateCircuit_Success() {
        SecurityContextHolder.setContext(securityContext);

        // Arrange
        Integer circuitId = 1;
        String username = "user123";
        Circuit existingCircuit = new Circuit();
        existingCircuit.setId(circuitId);
        existingCircuit.setName("Old Circuit");

        Circuit updatedCircuit = new Circuit();
        updatedCircuit.setName("New Circuit");
        updatedCircuit.setDistance(100.0);

        User user = new User();
        user.setUsername(username);

        Agency agency = new Agency();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(existingCircuit));
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(agencyRepository.findByOwner(user)).thenReturn(agency);
        when(agencyCircuitRepository.existsByAgencyAndCircuit(agency, existingCircuit)).thenReturn(true);
        when(circuitRepository.save(existingCircuit)).thenReturn(existingCircuit);

        // Act
        CircuitDTO result = agencyService.updateCircuit(circuitId, updatedCircuit);

        // Assert
        assertEquals("New Circuit", result.getName());
        assertEquals(100.0, result.getDistance());
    }

    @Test
    void testUpdateCircuit_CircuitNotFound() {
        SecurityContextHolder.setContext(securityContext);

        // Arrange
        Integer circuitId = 1;
        Circuit updatedCircuit = new Circuit();

        when(circuitRepository.findById(circuitId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CircuitNotFoundException.class, () -> agencyService.updateCircuit(circuitId, updatedCircuit),
                "Expected CircuitNotFoundException when circuit is not found");
    }
    @Test
    void testUpdateCircuit_CircuitNotLinkedToAgency() {        SecurityContextHolder.setContext(securityContext);

        // Arrange
        Integer circuitId = 1;
        String username = "user123";
        Circuit existingCircuit = new Circuit();
        existingCircuit.setId(circuitId);

        Circuit updatedCircuit = new Circuit();

        User user = new User();
        user.setUsername(username);

        Agency agency = new Agency();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(existingCircuit));
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(agencyRepository.findByOwner(user)).thenReturn(agency);
        when(agencyCircuitRepository.existsByAgencyAndCircuit(agency, existingCircuit)).thenReturn(false);

        // Act & Assert
        assertThrows(CircuitNotFoundException.class, () -> agencyService.updateCircuit(circuitId, updatedCircuit),
                "Expected CircuitNotFoundException when circuit is not linked to the user's agency");
    }
    @Test
    void testDeleteCircuit_Success() {
        SecurityContextHolder.setContext(securityContext);

        // Arrange
        Integer circuitId = 1;
        String username = "user123";

        Circuit circuit = new Circuit();
        circuit.setId(circuitId);

        User user = new User();
        user.setUsername(username);

        Agency agency = new Agency();

        AgencyCircuit agencyCircuit = new AgencyCircuit();
        agencyCircuit.setAgency(agency);
        agencyCircuit.setCircuit(circuit);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(agencyRepository.findByOwner(user)).thenReturn(agency);
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));
        when(agencyCircuitRepository.findByAgencyAndCircuit(agency, circuit)).thenReturn(Optional.of(agencyCircuit));

        // Act
        agencyService.deleteCircuit(circuitId);

        // Assert
        verify(agencyCircuitRepository, times(1)).deleteById(agencyCircuit.getId());
    }


    @Test
    void testDeleteCircuit_NoRelationBetweenAgencyAndCircuit() {
        SecurityContextHolder.setContext(securityContext);

        // Arrange
        Integer circuitId = 1;
        String username = "user123";

        Circuit circuit = new Circuit();
        circuit.setId(circuitId);

        User user = new User();
        user.setUsername(username);

        Agency agency = new Agency();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(agencyRepository.findByOwner(user)).thenReturn(agency);
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));
        when(agencyCircuitRepository.findByAgencyAndCircuit(agency, circuit)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CircuitNotFoundException.class, () -> agencyService.deleteCircuit(circuitId),
                "Expected CircuitNotFoundException when no relation exists between agency and circuit");
    }










}

