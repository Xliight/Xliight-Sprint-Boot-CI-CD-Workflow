package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.circuits.Step;
import com.ouitrips.app.entities.circuits.TrackingCircuit;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.TrackingCircuitMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.repositories.security.circuits.StepsRepository;
import com.ouitrips.app.repositories.security.circuits.TrackingCircuitRepository;
import com.ouitrips.app.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CircuitTrackingServiceImplTest {
    @Mock
    private TrackingCircuitRepository trackingCircuitRepository;
    @Mock
    private TrackingCircuitMapper trackingCircuitMapper;

    @Mock
    private CircuitRepository circuitRepository;

    @Mock
    private StepsRepository stepsRepository;

    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private CircuitTrackingServiceImpl circuitTrackingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTrackingCircuit_Success() {
        // Arrange
        Integer circuitReference = 1;
        Integer stepReference = 1;
        String dateStr = "2023-10-01";
        String latitude = "52.5200";
        String longitude = "13.4050";

        Circuit circuit = new Circuit();
        Step step = new Step();
        Instant date = Instant.now();

        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", circuitReference);
        List<Map<String, Object>> places = new ArrayList<>();
        Map<String, Object> place = new HashMap<>();
        place.put("date", dateStr);
        place.put("latitude", latitude);
        place.put("longitude", longitude);
        place.put("stepReference", stepReference);
        places.add(place);
        params.put("places", places);

        when(circuitRepository.findById(circuitReference)).thenReturn(Optional.of(circuit));
        when(stepsRepository.findById(stepReference)).thenReturn(Optional.of(step));
        when(dateUtil.createDateWithFormat(dateStr)).thenReturn(Date.from(date));
        // Act
        Object result = circuitTrackingService.save(params);
        // Assert
        assertNotNull(result);
        assertTrue(((Map<String, String>) result).containsKey("message"));
        assertEquals("Tracking circuits saved successfully", ((Map<String, String>) result).get("message"));
        verify(trackingCircuitRepository, times(1)).save(any(TrackingCircuit.class));
    }



    @Test
    void testSave_WithCircuitReference() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", 1);
        Map<String, Object> placeData = new HashMap<>();
        placeData.put("date", "2024-10-14T10:00:00Z");
        placeData.put("latitude", "40.7128");
        placeData.put("longitude", "-74.0060");
        placeData.put("stepReference", null); // No step reference provided

        List<Map<String, Object>> places = Collections.singletonList(placeData);
        params.put("places", places);

        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));
        when(dateUtil.createDateWithFormat(anyString())).thenReturn(Date.from(Instant.parse("2024-10-14T10:00:00Z")));

        // Act
        Object result = circuitTrackingService.save(params);

        // Assert
        assertEquals("Tracking circuits saved successfully", ((Map<String, Object>) result).get("message"));
        verify(trackingCircuitRepository, times(1)).save(any(TrackingCircuit.class)); // Verify save is called once
    }

    @Test
    void testSave_WithNoPlaces() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", 1);
        params.put("places", null); // No places provided

        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));

        // Act
        Object result = circuitTrackingService.save(params);

        // Assert
        assertEquals("Tracking circuits saved successfully", ((Map<String, Object>) result).get("message"));
        verify(trackingCircuitRepository, never()).save(any()); // Verify save is never called
    }
    @Test
    void testSave_WithStepReference() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", 1);
        Map<String, Object> placeData = new HashMap<>();
        placeData.put("date", "2024-10-14T10:00:00Z");
        placeData.put("latitude", "40.7128");
        placeData.put("longitude", "-74.0060");
        placeData.put("stepReference", 2); // Step reference provided
//dfs
        List<Map<String, Object>> places = Collections.singletonList(placeData);
        params.put("places", places);

        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));
        when(stepsRepository.findById(2)).thenReturn(Optional.of(new Step())); // Mock step retrieval
        when(dateUtil.createDateWithFormat(anyString())).thenReturn(Date.from(Instant.parse("2024-10-14T10:00:00Z")));

        // Act
        Object result = circuitTrackingService.save(params);

        // Assert
        assertEquals("Tracking circuits saved successfully", ((Map<String, Object>) result).get("message"));
        verify(trackingCircuitRepository, times(1)).save(any(TrackingCircuit.class)); // Verify save is called once
    }
//  Commited
    @Test
    void testSave_WithStepReference_NotFound() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", 1); // Assuming valid circuitReference
        Map<String, Object> placeData = new HashMap<>();
        placeData.put("date", "2024-10-14T10:00:00Z");
        placeData.put("latitude", "40.7128");
        placeData.put("longitude", "-74.0060");
        placeData.put("stepReference", 2); // Step reference that will not be found

        List<Map<String, Object>> places = Collections.singletonList(placeData);
        params.put("places", places);

        // Mock Circuit retrieval
        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));

        // Mock Step retrieval to return empty (step not found)
        when(stepsRepository.findById(2)).thenReturn(Optional.empty());

        // Mock DateUtil to return a valid date
        when(dateUtil.createDateWithFormat(anyString())).thenReturn(Date.from(Instant.parse("2024-10-14T10:00:00Z")));

        // Act & Assert
        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> circuitTrackingService.save(params)
        );

        // Verify exception message or content
        assertEquals("Step not found", exception.getContent());
    }

    @Test
    void testSave_WithCircuitReference_NotFound() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", 999); // Non-existent circuit reference

        when(circuitRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert

        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> circuitTrackingService.save(params)
        );
        assertEquals("Circuit not found", exception.getContent());
    }

    @Test
    void testSave_CircuitReferenceNull() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", null); // No circuit reference

        // Act
        Object result = circuitTrackingService.save(params);

        // Assert
        assertNotNull(result); // Check that the result is not null
        assertTrue(result instanceof Map); // Check if the result is a map
        assertEquals("Tracking circuits saved successfully", ((Map<String, Object>) result).get("message"));

        // Verify that repositories were not called
        verify(circuitRepository, never()).findById(anyInt());
        verify(stepsRepository, never()).findById(anyInt());
        verify(trackingCircuitRepository, never()).save(any(TrackingCircuit.class));
    }

    @Test
    void getTrackingAllByCircuit() {
        // Arrange
        Integer circuitReference = 1;
        Circuit circuit = new Circuit();
        circuit.setId(circuitReference);

        TrackingCircuit trackingCircuit1 = new TrackingCircuit();
        trackingCircuit1.setId(1);
        trackingCircuit1.setCircuit(circuit);
        trackingCircuit1.setStartDate(Instant.now());

        TrackingCircuit trackingCircuit2 = new TrackingCircuit();
        trackingCircuit2.setId(2);
        trackingCircuit2.setCircuit(circuit);
        trackingCircuit2.setStartDate(Instant.now());

        List<TrackingCircuit> trackingCircuits = List.of(trackingCircuit1, trackingCircuit2);
        when(trackingCircuitRepository.findByCircuitIdOrderByStartDate(circuitReference)).thenReturn(trackingCircuits);
        when(trackingCircuitMapper.apply(trackingCircuit1)).thenReturn(new Object());
        when(trackingCircuitMapper.apply(trackingCircuit2)).thenReturn(new Object());
        // Act
        List<Object> result = circuitTrackingService.getTrackingAllByCircuit(circuitReference);
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(trackingCircuitRepository, times(1)).findByCircuitIdOrderByStartDate(circuitReference);
        verify(trackingCircuitMapper, times(2)).apply(any(TrackingCircuit.class));

    }
}