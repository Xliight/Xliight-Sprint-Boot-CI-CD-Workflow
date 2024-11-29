package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.mapper.security.circuits.CircuitMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.services.places.impl.PlaceServiceImpl;
import com.ouitrips.app.utils.UserUtils;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class circuitget {

    @Mock
    private CircuitRepository circuitRepository;

    @InjectMocks
    private CircuitServiceImpl circuitServiceImpl;

    @Inject
    private CircuitServiceImpl forgetcirvuitServiceImpl;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserUtils userUtils;
    @Mock
    private Authentication authentication;
    @Mock
    private CircuitMapper circuitMapper;
    @Mock
    private PlaceServiceImpl placeService; // Mock for placeService

    //
    @Mock
    private User mockUser;
//    @Mock
//    private Circuit mockCircuiit;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up the security context

        // Set up the security context with a mocked authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    }
    @Test
    void testGetAll() {
//        User mockUser = new User();
//        mockUser.setId(3); // Set an ID or other properties as needed
        Circuit circuit1 = new Circuit();
        circuit1.setId(1);
        circuit1.setName("Circuit 1");
        Circuit circuit2 = new Circuit();
        circuit2.setId(2);
        circuit2.setName("Circuit 2");
        List<Circuit> mockCircuits = List.of(circuit1, circuit2);
        // Enure mock list is populated
        System.out.println("Mock Circuits List Size: " + mockCircuits.size()); // Should print 2
        // Mock repository behavior
        when(circuitRepository.findByIsDeleted(false)).thenReturn(mockCircuits);
        // Mock mapper behavior with additional logging
        when(circuitMapper.applyDetail(any(Circuit.class))).thenAnswer(invocation -> {
            Circuit circuit = invocation.getArgument(0);
            System.out.println("Mapping Circuit ID: " + circuit.getId() + ", Name: " + circuit.getName());
            return "Detailed Circuit " + circuit.getId();
        });
        // Act: Call the method under test
        Object result = circuitServiceImpl.getAll();
        // Assert: Verify the result
        assertNotNull(result);
        List<?> resultList = (List<?>) result;
        // Print the size of the resulting list
        System.out.println("Result List Size: " + resultList.size()); // Should print 2
        assertEquals(2, resultList.size(), "Expected 2 elements in the result list");
        // Check the detailed contents of the result
        assertEquals("Detailed Circuit 1", resultList.get(0));
        assertEquals("Detailed Circuit 2", resultList.get(1));
        // Verify interactions with mocks
        verify(circuitRepository, times(1)).findByIsDeleted(false);
        verify(circuitMapper, times(2)).applyDetail(any(Circuit.class));

    }

    @Test
    void testGetAllByUser() {
        // Arrange: Set up the user, circuits, and mock repository calls
        User mockUserr = new User();
        mockUserr.setId(1);
        Circuit circuit1 = new Circuit();
        circuit1.setId(1);
        circuit1.setName("Circuit 1");
        Circuit circuit2 = new Circuit();
        circuit2.setId(2);
        circuit2.setName("Circuit 2");
        List<Circuit> mockCircuitList = List.of(circuit1, circuit2);
        Object principal;
        // Mock behaviors
        when(authentication.getPrincipal()).thenReturn(mockUser); // Assuming User is returned as principal
        // Mock the behavior of userUtils to return the mock user
        when(userUtils.userAuthenticated()).thenReturn(mockUser);
        // Mock the behavior of circuitRepository
        when(circuitRepository.getAllByUser(mockUserr, false)).thenReturn(mockCircuitList);
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        // Act: Call the getAllByUser method
        Object result = circuitRepository.getAllByUser(mockUserr, false);
        // Assert: Verify the results
        List<Circuit> resultList = ((List<?>) result).stream().map(o -> (Circuit) o).collect(Collectors.toList());
        assertEquals(2, resultList.size());

}

    @Test
    void testGet_Circuit() {
        // Arrange
        Circuit mockCircuit = new Circuit();
        mockCircuit.setId(1);
        mockCircuit.setName("Test Circuit");
        mockCircuit.setIsDeleted(false);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("reference", mockCircuit.getId());
        expectedMap.put("name", mockCircuit.getName());
        expectedMap.put("distance", mockCircuit.getDistance());
        Object p;
        when(circuitRepository.findById(mockCircuit.getId())).thenReturn(Optional.of(mockCircuit));
        when(circuitMapper.apply(mockCircuit)).thenReturn(expectedMap);
        Object result = circuitServiceImpl.get(1);
        System.out.println(result);
        assertNotNull(result);
        assertEquals(expectedMap, result);
    }

    @Test
    void testDelete_CircuitExists() {
        // Arrange
        Circuit mockCircuit = new Circuit();
        mockCircuit.setId(1);
        mockCircuit.setIsDeleted(false);
        // Mock the behavior of getById
        when(circuitRepository.findById(1)).thenReturn(Optional.of(mockCircuit));
        // Act
        circuitServiceImpl.delete(1);
        // Assert
        assertTrue(mockCircuit.getIsDeleted()); // Check if the circuit is marked as deleted
        verify(circuitRepository, times(1)).save(mockCircuit); // Verify save was called
    }

    @Test
    void testGetLiveCircuits() {
        // Arrange
        Circuit circuit1 = new Circuit();
        circuit1.setId(1);
        circuit1.setName("Live Circuit 1");

        Circuit circuit2 = new Circuit();
        circuit2.setId(2);
        circuit2.setName("Live Circuit 2");

        List<Circuit> liveCircuits = List.of(circuit1, circuit2);

        // Mocking the behavior of findLiveCircuits
        when(circuitRepository.findLiveCircuits()).thenReturn(liveCircuits);

        // Mocking the behavior of the circuitMapper
        when(circuitMapper.apply(circuit1)).thenReturn(Map.of("id", 1, "name", "Live Circuit 1"));
        when(circuitMapper.apply(circuit2)).thenReturn(Map.of("id", 2, "name", "Live Circuit 2"));

        // Act
        List<Object> result = (List<Object>) circuitServiceImpl.getLiveCircuits();
        System.out.println("Result List Size: " + result.size());

        // Assert
        assertEquals(2, result.size());
        assertEquals(Map.of("id", 1, "name", "Live Circuit 1"), result.get(0));
        assertEquals(Map.of("id", 2, "name", "Live Circuit 2"), result.get(1));
        // Verify that findLiveCircuits and mapper were called
        verify(circuitRepository, times(1)).findLiveCircuits();
        verify(circuitMapper, times(1)).apply(circuit1);
        verify(circuitMapper, times(1)).apply(circuit2);
    }

    @Test
    void testSearch() {
        // Arrange
        String query = "test query";
        Map<String, Map<String, Object>> variables = new HashMap<>();

        Map<String, Object> searchParams = new HashMap<>();
        Map<String, Object> itinerary = new HashMap<>();
        Map<String, Object> source = new HashMap<>();
        source.put("ids", List.of("city1", "city2"));
        itinerary.put("source", source);
        searchParams.put("itinerary", itinerary);
        variables.put("search", searchParams);
        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("sourceCities", List.of("city1", "city2"));
        Map<String, Object> response = new HashMap<>();
        response.put("DailyItinerary", "10");
        response.put("TotalDurationVisited", "20");
        //placesSource===> (place4, place5)
        response.put("placesDestination","Rabat");
        // Mocking the behavior of placeService
        when(placeService.searchTestPlace(expectedParams)).thenReturn(response);
        // Act
        Object result = circuitServiceImpl.search(query, variables);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        System.out.println(resultMap);
//        // Assert
        assertEquals(response.get("DailyItinerary"), resultMap.get("DailyItinerary"));
        assertEquals(response.get("formattedTotalDurationVisited"), resultMap.get("formattedTotalDurationVisited"));
//        assertEquals(response.get("placesDestination"), resultMap.get("placesDestination"));
    }

}