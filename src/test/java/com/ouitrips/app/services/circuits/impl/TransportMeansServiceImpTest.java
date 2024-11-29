package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.TransportMeans;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.TransportMeansMapper;
import com.ouitrips.app.repositories.security.circuits.TransportMeansRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransportMeansServiceImpTest {
    @Mock
    private TransportMeansRepository transportMeansRepository;
    @Mock
    private TransportMeansMapper transportMeansMapper;

    @InjectMocks
    private TransportMeansServiceImp transportMeansService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    }

    @Test
    void save() {
        // Arrange
        Map<String, Object> params = Map.of("name", "Bus", "code", "B01");
        TransportMeans transportMeans = new TransportMeans();
        transportMeans.setId(1);

        when(transportMeansRepository.save(any(TransportMeans.class))).thenReturn(transportMeans);

        // Act
        Map<String, Object> result = (Map<String, Object>) transportMeansService.save(params);
        System.out.println(result);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("reference"));

        verify(transportMeansRepository, times(1)).save(any(TransportMeans.class));
    }

    @Test
    void testSave_WithName() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("name", "Bus");
        params.put("code", null);  // No code provided
        TransportMeans transportMeans = new TransportMeans();
        transportMeans.setId(1);

        when(transportMeansRepository.findById(1)).thenReturn(Optional.of(transportMeans));
        when(transportMeansRepository.save(any(TransportMeans.class))).thenReturn(transportMeans);

        // Act
        Object result = transportMeansService.save(params);

        // Assert
        assertEquals(transportMeans.getId(), ((Map<String, Object>) result).get("reference"));
        assertEquals("Bus", transportMeans.getName());  // Name should be set
        assertEquals(null, transportMeans.getCode());    // Code should remain null
    }

    @Test
    void testSave_WithCode() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("name", null);  // No name provided
        params.put("code", "TM001");
        TransportMeans transportMeans = new TransportMeans();
        transportMeans.setId(1);
        when(transportMeansRepository.findById(1)).thenReturn(Optional.of(transportMeans));
        when(transportMeansRepository.save(any(TransportMeans.class))).thenReturn(transportMeans);

        // Act
        Object result = transportMeansService.save(params);

        // Assert
        assertEquals(transportMeans.getId(), ((Map<String, Object>) result).get("reference"));
        assertEquals(null, transportMeans.getName());  // Name should remain null
        assertEquals("TM001", transportMeans.getCode());  // Code should be set
    }

    @Test
    void testSave_ExistingTransportMeans() {
        // Arrange
        Integer id = 1;
        TransportMeans existingTransportMeans = new TransportMeans();
        existingTransportMeans.setId(id);
        existingTransportMeans.setName("Bus");
        existingTransportMeans.setCode("B01");

        when(transportMeansRepository.findById(id)).thenReturn(Optional.of(existingTransportMeans));
        // New values to update
        Map<String, Object> params = Map.of("id", id, "name", "Train", "code", "T01");
        when(transportMeansRepository.save(existingTransportMeans)).thenReturn(existingTransportMeans);
        // Act
        Map<String, Object> result = (Map<String, Object>) transportMeansService.save(params);
          // Assert
        assertNotNull(result);
        assertEquals(1, result.get("reference"));
        // Check that the transport means was updated
        assertEquals("Train", existingTransportMeans.getName());  // Updated name
        assertEquals("T01", existingTransportMeans.getCode());    // Updated code
        // Verify that save was called with the updated entity
        verify(transportMeansRepository, times(1)).save(existingTransportMeans);
        // Additionally, you could verify that the original values are no longer present
        assertNotEquals("Bus", existingTransportMeans.getName());
        assertNotEquals("B01", existingTransportMeans.getCode());
    }

    @Test
    void delete() {
        // Arrange
        Integer id = 1;
        TransportMeans transportMeans = new TransportMeans();
        when(transportMeansRepository.findById(id)).thenReturn(Optional.of(transportMeans));
        // Act
        transportMeansService.delete(id);
        // Assert
        verify(transportMeansRepository, times(1)).delete(transportMeans);
    }

    @Test
    void get() {
        // Arrange
        Integer id = 1;
        TransportMeans transportMeans = new TransportMeans();
        when(transportMeansRepository.findById(id)).thenReturn(Optional.of(transportMeans));

        Object transportMeansDto = new Object();
        when(transportMeansMapper.apply(transportMeans)).thenReturn(transportMeansDto);

        // Act
        Object result = transportMeansService.get(id);

        // Assert
        assertNotNull(result);
        assertEquals(transportMeansDto, result);
        verify(transportMeansRepository, times(1)).findById(id);
        verify(transportMeansMapper, times(1)).apply(transportMeans);
    }
    @Test
    void testGetById_NotFound() {
        // Arrange
        Integer id = 1;

        when(transportMeansRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> transportMeansService.getById(id)
        );
        System.out.println(exception.getContent());
        assertEquals("Mode Of Transport not found", exception.getContent());
    }

    @Test
    void getAll() {
        List<TransportMeans> transportMeansList = List.of(new TransportMeans(), new TransportMeans());
        when(transportMeansRepository.findAll()).thenReturn(transportMeansList);

        Object transportMeansDto1 = new Object();
        Object transportMeansDto2 = new Object();
        when(transportMeansMapper.apply(transportMeansList.get(0))).thenReturn(transportMeansDto1);
        when(transportMeansMapper.apply(transportMeansList.get(1))).thenReturn(transportMeansDto2);

        // Act
        List<Object> result = (List<Object>) transportMeansService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transportMeansDto1, result.get(0));
        assertEquals(transportMeansDto2, result.get(1));
        verify(transportMeansRepository, times(1)).findAll();
        verify(transportMeansMapper, times(1)).apply(transportMeansList.get(0));
        verify(transportMeansMapper, times(1)).apply(transportMeansList.get(1));
    }
}