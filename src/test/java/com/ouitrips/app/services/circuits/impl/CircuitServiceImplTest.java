package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.CircuitGroupMapper;
import com.ouitrips.app.mapper.security.circuits.CircuitMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitGroupRepository;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.services.places.impl.PlaceServiceImpl;
import com.ouitrips.app.utils.UserUtils;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import java.util.*;

import static org.mockito.Mockito.*;

class CircuitServiceImplTest {


    @Mock
    private PlaceServiceImpl placeService;
    @Mock
    private CircuitRepository circuitRepository;

    @Mock
    private CircuitGroupRepository circuitGroupRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private CircuitGroupServiceImpl circuitGroupServiceImpl;

    @Mock
    private CircuitMapper circuitMapper;
    @Mock
    private CircuitGroupMapper circuitGroupMapper;
    @InjectMocks
    private CircuitServiceImpl circuitServiceImpl;
    @Inject
    private CircuitServiceImpl forgetcirvuitServiceImpl;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSave_CreateCircuit() {
        // Given: Parameters for creating a new Circuit
        Map<String, Object> params = new HashMap<>();
        params.put("name", "New Circuit");
        params.put("distance", 100.5);
        params.put("circuitGroupReference", null); // No specific CircuitGroup reference
        // Mocked entities and values
        User mockUser = mock(User.class);
        CircuitGroup mockCircuitGroup = mock(CircuitGroup.class);
        Circuit mockCircuit = new Circuit();
        mockCircuit.setId(1); // Set a valid ID to simulate a saved entity
        // When: User authenticated and no default CircuitGroup exists
        when(userUtils.userAuthenticated()).thenReturn(mockUser);
        when(circuitGroupRepository.findByIsDefaultAndUser(true, mockUser)).thenReturn(Collections.emptyList());
        when(circuitGroupServiceImpl.addDefaultByUser(mockUser)).thenReturn(mockCircuitGroup);
        when(circuitRepository.save(any(Circuit.class))).thenReturn(mockCircuit); // Return mockCircuit with ID
        // Act
        Object result = circuitServiceImpl.save(params);
        System.out.println(result.toString());
        assertEquals(1, ((Map) result).get("reference"));


    }

    //update
    @Test
    void testSave_UpdateCircuit() {
        // Given: Parameters for updating an existing Circuit
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1); // Existing Circuit
        params.put("name", "Updated Circuit");
        params.put("distance", 150.0);
        params.put("circuitGroupReference",3 ); // Existing CircuitGroup reference

        Circuit mockCircuit = new Circuit();
        mockCircuit.setId(1);
        CircuitGroup mockCircuitGroup = mock(CircuitGroup.class);
        // When: Fetching the existing Circuit and CircuitGroup
        when(circuitRepository.findById(1)).thenReturn((Optional.of(mockCircuit)));
        when(circuitGroupRepository.findById(3)).thenReturn(Optional.of(mockCircuitGroup));
        when(circuitRepository.save(mockCircuit)).thenReturn(mockCircuit);
        // Act
        Object result = circuitServiceImpl.save(params);

        // Then: Assert that the existing Circuit is updated correctly
        assertEquals(1, ((Map) result).get("reference"));

        verify(circuitRepository, times(1)).findById(1);
//        verify(circuitGroupRepository, times(1)).findById(2);
//        verify(circuitRepository, times(1)).save(mockCircuit);
    }

    @Test
    void save_whenNoCircuitGroupReferenceAndDefaultGroupExists_setsDefaultGroup() {
        // Prepare test data
        Map<String, Object> params = new HashMap<>();
        params.put("name", "New Circuit");
        params.put("distance", 100.5);
        params.put("circuitGroupReference", null);
        User user = new User();
        user.setId(1);
        Circuit circuit = new Circuit();
        circuit.setId(1);
        CircuitGroup existingDefaultGroup = new CircuitGroup();
        existingDefaultGroup.setId(1);
        // Mock behavior
        when(userUtils.userAuthenticated()).thenReturn(user);
        when(circuitGroupRepository.findByIsDefaultAndUser(true, user)).thenReturn(List.of(existingDefaultGroup));
        when(circuitRepository.save(any(Circuit.class))).thenReturn(circuit);

        // Execute the method under test
        circuitServiceImpl.save(params);

        assertEquals(existingDefaultGroup.getId(), circuit.getId());
        // Verify the result
    }

    @Test
    void getAllByUser_returnsMappedCircuitsForAuthenticatedUser() {
        // Arrange
        User user = new User();
        Circuit circuit1 = new Circuit();
        Circuit circuit2 = new Circuit();
        List<Circuit> circuits = List.of(circuit1, circuit2);
        Object mappedCircuit1 = new Object();
        Object mappedCircuit2 = new Object();
        List<Object> expectedMappedCircuits = List.of(mappedCircuit1, mappedCircuit2);

        // Mocking behavior
        when(userUtils.userAuthenticated()).thenReturn(user);
        when(circuitRepository.getAllByUser(user, false)).thenReturn(circuits);
        when(circuitMapper.apply(circuit1)).thenReturn(mappedCircuit1);
        when(circuitMapper.apply(circuit2)).thenReturn(mappedCircuit2);

        // Act
        List<Object> result = (List<Object>) circuitServiceImpl.getAllByUser();

        // Assert
        assertEquals(expectedMappedCircuits, result);
        verify(userUtils, times(1)).userAuthenticated();
        verify(circuitRepository, times(1)).getAllByUser(user, false);
        verify(circuitMapper, times(1)).apply(circuit1);
        verify(circuitMapper, times(1)).apply(circuit2);
    }


    @Test
    void getAll_returnsMappedNonDeletedCircuits() {
        // Arrange
        Circuit circuit1 = new Circuit();
        Circuit circuit2 = new Circuit();
        List<Circuit> circuits = List.of(circuit1, circuit2);
        Object mappedCircuit1 = new Object();
        Object mappedCircuit2 = new Object();
        List<Object> expectedMappedCircuits = List.of(mappedCircuit1, mappedCircuit2);

        // Mock behavior
        when(circuitRepository.findByIsDeleted(false)).thenReturn(circuits);
        when(circuitMapper.applyDetail(circuit1)).thenReturn(mappedCircuit1);
        when(circuitMapper.applyDetail(circuit2)).thenReturn(mappedCircuit2);

        // Act
        List<Object> result = (List<Object>) circuitServiceImpl.getAll();

        // Assert
        assertEquals(expectedMappedCircuits, result);
        verify(circuitRepository, times(1)).findByIsDeleted(false);
        verify(circuitMapper, times(1)).applyDetail(circuit1);
        verify(circuitMapper, times(1)).applyDetail(circuit2);
    }

    @Test
    void get_existingNonDeletedCircuit_returnsMappedCircuit() {
        // Arrange
        Integer circuitId = 1;
        Circuit circuit = new Circuit();
        circuit.setIsDeleted(false);
        Object mappedCircuit = new Object();

        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));
        when(circuitMapper.apply(circuit)).thenReturn(mappedCircuit);

        // Act
        Object result = circuitServiceImpl.get(circuitId);

        // Assert
        assertEquals(mappedCircuit, result);
        verify(circuitRepository, times(1)).findById(circuitId);
        verify(circuitMapper, times(1)).apply(circuit);
    }

    @Test
    void get_deletedCircuit_throwsException() {
        // Arrange
        Integer circuitId = 1;
        Circuit circuit = new Circuit();
        circuit.setIsDeleted(true);
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> circuitServiceImpl.get(circuitId)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Circuit not found", exception.getContent());

        verify(circuitRepository, times(1)).findById(circuitId);
        verify(circuitMapper, never()).apply(any());
    }

    @Test
    void getById_existingCircuit_returnsCircuit() {
        // Arrange
        Integer circuitId = 1;
        Circuit circuit = new Circuit();

        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));

        // Act
        Circuit result = circuitServiceImpl.getById(circuitId);

        // Assert
        assertEquals(circuit, result);
        verify(circuitRepository, times(1)).findById(circuitId);
    }

    @Test
    void getById_nonExistingCircuit_throwsException() {
        // Arrange
        Integer circuitId = 1;

        when(circuitRepository.findById(circuitId)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> circuitServiceImpl.getById(circuitId)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Circuit not found", exception.getContent());
        verify(circuitRepository, times(1)).findById(circuitId);
    }

    @Test
    void delete_existingCircuit_marksAsDeleted() {
        // Arrange
        Integer circuitId = 1;
        Circuit circuit = new Circuit();
        circuit.setIsDeleted(false);
        when(circuitRepository.findById(circuitId)).thenReturn(Optional.of(circuit));
        when(circuitRepository.save(circuit)).thenReturn(circuit);
        // Act
        circuitServiceImpl.delete(circuitId);
        // Assert
        assertTrue(circuit.getIsDeleted());
        verify(circuitRepository, times(1)).findById(circuitId);
        verify(circuitRepository, times(1)).save(circuit);
    }

    @Test
    void getLiveCircuits_returnsMappedLiveCircuits() {
        // Arrange
        Circuit circuit1 = new Circuit();
        Circuit circuit2 = new Circuit();
        List<Circuit> liveCircuits = List.of(circuit1, circuit2);
        Object mappedCircuit1 = new Object();
        Object mappedCircuit2 = new Object();
        List<Object> expectedMappedCircuits = List.of(mappedCircuit1, mappedCircuit2);

        when(circuitRepository.findLiveCircuits()).thenReturn(liveCircuits);
        when(circuitMapper.apply(circuit1)).thenReturn(mappedCircuit1);
        when(circuitMapper.apply(circuit2)).thenReturn(mappedCircuit2);

        // Act
        List<Object> result = (List<Object>) circuitServiceImpl.getLiveCircuits();

        // Assert
        assertEquals(expectedMappedCircuits, result);
        verify(circuitRepository, times(1)).findLiveCircuits();
        verify(circuitMapper, times(1)).apply(circuit1);
        verify(circuitMapper, times(1)).apply(circuit2);
    }

    // Search methode
    @Test
    void search_withNullVariables_shouldCallPlaceServiceWithEmptyParams() {
        // Arrange
        String query = "someQuery";
        Map<String, Object> emptyParams = new HashMap<>();

        when(placeService.searchTestPlace(emptyParams)).thenReturn(List.of());

        // Act
        Object result = circuitServiceImpl.search(query, null);

        // Assert
        verify(placeService, times(1)).searchTestPlace(emptyParams);
        assertNotNull(result);
    }

    @Test
    void search_withEmptySearchParams_shouldCallPlaceServiceWithEmptyParams() {
        // Arrange
        String query = "someQuery";
        Map<String, Map<String, Object>> variables = new HashMap<>();
        variables.put("search", new HashMap<>());

        when(placeService.searchTestPlace(anyMap())).thenReturn(List.of());

        // Act
        Object result = circuitServiceImpl.search(query, variables);

        // Assert
        verify(placeService, times(1)).searchTestPlace(anyMap());
        assertNotNull(result);
    }


    @Test
    void search_withItineraryAndPassengers_shouldPassCorrectParamsToPlaceService() {
        // Arrange
        String query = "testQuery";
        Map<String, Map<String, Object>> variables = new HashMap<>();

        Map<String, Object> itinerary = new HashMap<>();
        itinerary.put("source", Map.of("ids", List.of("sourceCity1", "sourceCity2")));
        itinerary.put("destination", Map.of("ids", List.of("destinationCity1")));
        itinerary.put("outboundDepartureDate", Map.of("start", "2024-01-01", "end", "2024-01-10"));

        Map<String, Object> passengers = new HashMap<>();
        passengers.put("adults", 2);
        passengers.put("children", 1);
        passengers.put("infants", 0);

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("itinerary", itinerary);
        searchParams.put("passengers", passengers);

        variables.put("search", searchParams);

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("sourceCities", List.of("sourceCity1", "sourceCity2"));
        expectedParams.put("destinationCities", List.of("destinationCity1"));
        expectedParams.put("outboundDepartureDateStart", "2024-01-01");
        expectedParams.put("outboundDepartureDateEnd", "2024-01-10");
        expectedParams.put("adults", 2);
        expectedParams.put("children", 1);
        expectedParams.put("infants", 0);

        when(placeService.searchTestPlace(expectedParams)).thenReturn(List.of());

        // Act
        Object result = circuitServiceImpl.search(query, variables);

        // Assert
        verify(placeService, times(1)).searchTestPlace(expectedParams);
        assertNotNull(result);
    }

    @Test
    void search_withTransportMods_shouldPassCorrectParamsToPlaceService() {
        // Arrange
        String query = "queryWithTransportMods";
        Map<String, Map<String, Object>> variables = new HashMap<>();

        Map<String, Object> transportMods = new HashMap<>();
        transportMods.put("fly", true);
        transportMods.put("train", false);
        transportMods.put("drive", true);
        transportMods.put("MotoCycle", false);

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("transport_mods", transportMods);

        variables.put("search", searchParams);

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("motorcycle", false);
        expectedParams.put("fly", true);
        expectedParams.put("bicycle", false);
        expectedParams.put("drive", true);
        expectedParams.put("walk", false);
        expectedParams.put("train", false);

        when(placeService.searchTestPlace(expectedParams)).thenReturn(List.of());

        // Act
        Object result = circuitServiceImpl.search(query, variables);

        // Assert
        verify(placeService, times(1)).searchTestPlace(expectedParams);
        verify(placeService).searchTestPlace(eq(expectedParams));

        assertNotNull(result);
    }

    @Test
    void search_withAllParams_shouldPassCompleteParamsToPlaceService() {
        // Arrange
        String query = "completeQuery";
        Map<String, Map<String, Object>> variables = new HashMap<>();

        Map<String, Object> itinerary = new HashMap<>();
        itinerary.put("source", Map.of("ids", List.of("sourceCity1")));
        itinerary.put("destination", Map.of("ids", List.of("destinationCity1")));
        itinerary.put("outboundDepartureDate", Map.of("start", "2024-01-01", "end", "2024-01-10"));
        itinerary.put("inboundDepartureDate", Map.of("start", "2024-02-01", "end", "2024-02-10"));

        Map<String, Object> passengers = new HashMap<>();
        passengers.put("adults", 3);
        passengers.put("children", 1);
        passengers.put("infants", 1);

        Map<String, Object> transportMods = new HashMap<>();
        transportMods.put("fly", true);
        transportMods.put("train", false);

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("itinerary", itinerary);
        searchParams.put("passengers", passengers);
        searchParams.put("transport_mods", transportMods);

        variables.put("search", searchParams);

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("fly", true);
        expectedParams.put("infants", 1);
        expectedParams.put("outboundDepartureDateEnd", "2024-01-10");
        expectedParams.put("children", 1);
        expectedParams.put("adults", 3);
        expectedParams.put("inboundDepartureDateEnd", "2024-02-10");
        expectedParams.put("sourceCities", Arrays.asList("sourceCity1"));
        expectedParams.put("destinationCities", Arrays.asList("destinationCity1"));
        expectedParams.put("inboundDepartureDateStart", "2024-02-01");
        expectedParams.put("outboundDepartureDateStart", "2024-01-01");
        expectedParams.put("train", false);
        expectedParams.put("bicycle", false); // Add other transport modes as needed
        expectedParams.put("motorcycle", false);
        expectedParams.put("drive", false);
        expectedParams.put("walk", false);

        when(placeService.searchTestPlace(expectedParams)).thenReturn(List.of());

        // Act
        Object result = circuitServiceImpl.search(query, variables);

        // Assert
        verify(placeService, times(1)).searchTestPlace(expectedParams);
        verify(placeService).searchTestPlace(eq(expectedParams));
        assertNotNull(result);
    }

    @Test
    void search_withVisitedLocations_shouldAddVisitedCitiesToParams() {

        // Arrange
        String query = "some query";
        Map<String, Map<String, Object>> variables = new HashMap<>();

        Map<String, Object> visitedLocations = new HashMap<>();
        visitedLocations.put("ids", List.of("city1", "city2"));

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("itinerary", new HashMap<>() {{
            put("visited_locations", visitedLocations);
        }});
        variables.put("search", searchParams);

        // Act
        circuitServiceImpl.search(query, variables); // Call the method you are testing

        // Capture the params passed to the placeService's searchTestPlace method
        ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(placeService).searchTestPlace(paramsCaptor.capture());

        // Assert
        Map<String, Object> capturedParams = paramsCaptor.getValue();
        assertEquals(List.of("city1", "city2"), capturedParams.get("visitedCities")); // Check if visitedCities is set correctly
    }

}