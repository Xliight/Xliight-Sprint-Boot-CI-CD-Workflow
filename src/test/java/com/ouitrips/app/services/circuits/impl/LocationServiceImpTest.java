package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.LinksLs;
import com.ouitrips.app.entities.circuits.Location;
import com.ouitrips.app.entities.circuits.SpecificPlace;
import com.ouitrips.app.entities.circuits.Step;
import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.LocationsMapper;
import com.ouitrips.app.repositories.security.circuits.LinksLsRepository;
import com.ouitrips.app.repositories.security.circuits.LocationsRepository;
import com.ouitrips.app.repositories.security.circuits.SpecificPlaceRepository;
import com.ouitrips.app.repositories.security.circuits.StepsRepository;
import com.ouitrips.app.repositories.security.places.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.*;

class LocationServiceImpTest {
    @Mock
    private LocationsRepository locationsRepository;
    @Mock
    private LocationsMapper locationsMapper;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private SpecificPlaceRepository specificPlaceRepository;
    @Mock
    private StepsRepository stepsRepository;
    @Mock
    private LinksLsRepository linksLsRepository;
    @InjectMocks
    private LocationServiceImp locationServiceImp;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void save() {
        Map<String, Object> params = new HashMap<>();
        params.put("description", "Beautiful place");
        params.put("latitude", 34.00);
        params.put("longitude", -6.85);
        params.put("location", "Rabat");
        params.put("name", "Place Name");
        params.put("rating", 5);
        params.put("placeReference", 1);
        params.put("placeSpecReference", 2);

        Place place = new Place();
        place.setId(1);
        SpecificPlace specificPlace = new SpecificPlace();
        specificPlace.setId(2);

        when(placeRepository.findById(1)).thenReturn(Optional.of(place));
        when(specificPlaceRepository.findById(2)).thenReturn(Optional.of(specificPlace));
        Location loc = new Location();
        loc.setId(100);
        Location savedLocation = new Location();
        savedLocation.setId(100);
        when(locationsRepository.save(argThat(location -> location instanceof Location))).thenReturn(savedLocation);

        // Act
        Map<String, Object> result = (Map<String, Object>) locationServiceImp.save(params);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.get("reference"));
        verify(placeRepository, times(1)).findById(1);
        verify(specificPlaceRepository, times(1)).findById(2);

    }

    @Test
    void testSave_UpdateExistingLocation() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("description", "Updated description");

        Location existingLocation = new Location();
        existingLocation.setId(1);
        existingLocation.setDescription("Old description");

        when(locationsRepository.findById(1)).thenReturn(Optional.of(existingLocation));
        when(locationsRepository.save(argThat(location -> location instanceof Location))).thenReturn(existingLocation);

        // Act
        Map<String, Object> result = (Map<String, Object>) locationServiceImp.save(params);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("reference"));
        assertEquals("Updated description", existingLocation.getDescription());
        verify(locationsRepository, times(1)).findById(1);
//        verify(locationsRepository, times(1)).save(any(Location.class));
    }

    @Test
    void testSave_WithNonNullDescription() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("description", "A new location");

        Location loc = new Location();
        loc.setId(1);
        when(locationsRepository.save(argThat(location -> location instanceof Location))).thenReturn(loc);

        // Act
        Object result = locationServiceImp.save(params);
        assertNotNull(result);
    }

    @Test
    void testSave_PlaceNotFound() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("placeReference", 1);

        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> locationServiceImp.save(params)
        );
        assertEquals("Place with ID 1 not found", exception.getContent());
    }
//
    @Test
    void testSave_SpecificPlaceNotFound() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("placeSpecReference", 2);

        when(specificPlaceRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> locationServiceImp.save(params)
        );
        assertEquals("specificPlace with ID 2 not found", exception.getContent());
    }

    @Test
    void getLocationByStep() {
        // Arrange
        Integer stepId = 1;
        Step step = new Step();
        when(stepsRepository.findById(stepId)).thenReturn(Optional.of(step));

        LinksLs link = new LinksLs();
        Location startLocation = new Location();
        Location endLocation = new Location();
        link.setLocationStart(startLocation);
        link.setLocationEnd(endLocation);

        List<LinksLs> linksList = List.of(link);
        when(linksLsRepository.findByStep(step)).thenReturn(linksList);

        Object startLocationDto = new Object();
        Object endLocationDto = new Object();
        when(locationsMapper.apply(startLocation)).thenReturn(startLocationDto);
        when(locationsMapper.apply(endLocation)).thenReturn(endLocationDto);

        // Act
        List<Object> result = locationServiceImp.getLocationByStep(stepId);
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(startLocationDto, result.get(0));
        assertEquals(endLocationDto, result.get(1));
        verify(stepsRepository, times(1)).findById(stepId);
        verify(linksLsRepository, times(1)).findByStep(step);
        verify(locationsMapper, times(1)).apply(startLocation);
        verify(locationsMapper, times(1)).apply(endLocation);
//
    }

    @Test
    void testGetLocationByStep_WhenLinksEmpty() {
        // Arrange
        Integer stepId = 1;
        Step step = new Step();

        // Set up mocks
        when(stepsRepository.findById(stepId)).thenReturn(Optional.of(step));
        when(linksLsRepository.findByStep(step)).thenReturn(Collections.emptyList());

        // Act
        List<Object> result = locationServiceImp.getLocationByStep(stepId);

        // Assert
        assertTrue(result.isEmpty(), "Step not found");
        verify(linksLsRepository).findByStep(step); // Verify repository interaction
    }
    @Test
    void testGetLocationByStep_StepNotFound() {
        // Arrange
        Integer stepId = 1;

        // Set up mocks
        when(stepsRepository.findById(stepId)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> locationServiceImp.getLocationByStep(stepId)
        );
        assertEquals("Step not found", exception.getContent());
    }


    @Test
    void getAll() {
        // Arrange
        List<Location> locations = List.of(new Location(), new Location());
        when(locationsRepository.findAll()).thenReturn(locations);

        Object locationDto1 = new Object();
        Object locationDto2 = new Object();
        when(locationsMapper.apply(locations.get(0))).thenReturn(locationDto1);
        when(locationsMapper.apply(locations.get(1))).thenReturn(locationDto2);
        // Act
        List<Object> result = locationServiceImp.getAll();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(locationDto1, result.get(0));
        assertEquals(locationDto2, result.get(1));
        verify(locationsRepository, times(1)).findAll();
        verify(locationsMapper, times(1)).apply(locations.get(0));
        verify(locationsMapper, times(1)).apply(locations.get(1));
    }

    @Test
    void get() {
        Integer id = 1;
        Location location = new Location();
        when(locationsRepository.findById(id)).thenReturn(Optional.of(location));
        Object locationDto = new Object();
        when(locationsMapper.apply(location)).thenReturn(locationDto);
        // Act
        Object result = locationServiceImp.get(id);
        // Assert
        assertNotNull(result);
        assertEquals(locationDto, result);
        verify(locationsRepository, times(1)).findById(id);
        verify(locationsMapper, times(1)).apply(location);
    }


    @Test
    void testGetById_NotFound() {
        // Arrange
        Integer id = 1;
        when(locationsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> locationServiceImp.get(id)
        );
        assertEquals("Location not found", exception.getContent());
    }


    @Test
    void delete() {
        // Arrange
        Integer id = 1;
        Location location = new Location();
        when(locationsRepository.findById(id)).thenReturn(Optional.of(location));

        // Act
        locationServiceImp.delete(id);

        // Assert
        verify(locationsRepository, times(1)).findById(id);
        verify(locationsRepository, times(1)).delete(location);
    }

}