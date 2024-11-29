package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.*;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.StepsMapper;
import com.ouitrips.app.repositories.security.circuits.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StepsServiceImplTest {
    @Mock
    private StepsRepository stepsRepository;
    @Mock
    private StepsMapper stepsMapper;
    @Mock
    private LinksLsRepository linksLsRepository;
    @Mock
    private LocationsRepository locationRepository;
    @Mock
    private TransportMeansRepository transportMeansRepository;
    @Mock
    private CircuitRepository circuitRepository;
    @InjectMocks
    private StepsServiceImpl stepsService;
    private Map<String, Object> params;
    private static final String ORDER_STEP = "orderStep"; // Adjust if necessary

    private LinksLs newLinksLs;
    private LinksLsId linksLsId;
    private String locationName;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Define parameters for the save method

    }

    @Test
    public void testSave_NewStep() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", null); // Testing creation of a new step
        params.put("directions", "North");
        params.put("name", "Step Name");
        params.put("orderStep", 1);
        params.put("state", true);
        params.put("duration", 10.5f);
        params.put("distance", 100.0f);
        params.put("circuitReference", 1);
        params.put("locationReference", 2);
        params.put("LocationEndReference", 3);
        params.put("ModeReference", 4);


        Location locationStart = new Location();
        locationStart.setId(2);
        Location locationEnd = new Location();
        locationEnd.setId(3);
        TransportMeans mode = new TransportMeans();
        mode.setId(4);
        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));
        when(locationRepository.findById(2)).thenReturn(Optional.of(locationStart));
        when(locationRepository.findById(3)).thenReturn(Optional.of(locationEnd));
        when(transportMeansRepository.findById(4)).thenReturn(Optional.of(mode));
        when(stepsRepository.save(any(Step.class))).thenAnswer(invocation -> {
            Step step = invocation.getArgument(0);
            step.setId(1); // Mock setting the ID after saving
            return step;
        });

        Map<String, Object> result = (Map<String, Object>) stepsService.save(params);

        assertNotNull(result);
        assertEquals(1, result.get("reference"));
    }


    @Test
    public void testSave_UpdateStep() {
        // Setup: Define existing Step, LinksLs, Circuit, Location, and TransportMeans entities
        // Setup: Define existing Step, LinksLs, Circuit, Location, and TransportMeans entities
        Integer stepId = 1; // assume this ID exists in your database
        Integer circuitReference = 1; // assume this Circuit ID exists
        Integer locationReference = 1; // assume this Location ID exists
        Integer locationEndReference = 2; // assume this Location End ID exists
        Integer modeReference = 1; // assume this TransportMeans ID exists

        // Existing Step setup
        Step existingStep = new Step();
        existingStep.setId(stepId);
        existingStep.setName("Existing Step");
        existingStep.setDirections("Existing directions");
        existingStep.setOrderStep(1);
        existingStep.setLinks(new HashSet<>()); // Initialize the links set

        // Mock data for dependencies
        Circuit circuit = new Circuit();
        circuit.setId(circuitReference);
        Location locationStart = new Location();
        locationStart.setId(locationReference);
        Location locationEnd = new Location();
        locationEnd.setId(locationEndReference);
        TransportMeans mode = new TransportMeans();
        mode.setId(modeReference);

        LinksLs existingLinksLs = new LinksLs();
        LinksLsId linksLsId = new LinksLsId();
        linksLsId.setFkidStep(stepId);
        linksLsId.setFkidLocationStart(locationReference);
        linksLsId.setFkidLocationEnd(locationEndReference);
        linksLsId.setFkidMode(modeReference);
        existingLinksLs.setId(linksLsId);
        existingLinksLs.setStep(existingStep);
        existingLinksLs.setLocationStart(locationStart);
        existingLinksLs.setLocationEnd(locationEnd);
        existingLinksLs.setMode(mode);
        existingStep.getLinks().add(existingLinksLs);

        // Mocks for repositories
        when(stepsRepository.findById(stepId)).thenReturn(Optional.of(existingStep));
        when(circuitRepository.findById(circuitReference)).thenReturn(Optional.of(circuit));
        when(locationRepository.findById(locationReference)).thenReturn(Optional.of(locationStart));
        when(locationRepository.findById(locationEndReference)).thenReturn(Optional.of(locationEnd));
        when(transportMeansRepository.findById(modeReference)).thenReturn(Optional.of(mode));

        // Prepare params for the update
        Map<String, Object> params = new HashMap<>();
        params.put("id", stepId);
        params.put("name", "Updated Step Name");
        params.put("directions", "Updated directions");
        params.put("orderStep", 2);
        params.put("state", true);
        params.put("duration", 3.5f);
        params.put("distance", 150.0f);
        params.put("circuitReference", circuitReference);
        params.put("locationReference", locationReference);
        params.put("LocationEndReference", locationEndReference);
        params.put("ModeReference", modeReference);
        // Call the save method
        Map<String, Object> result = (Map<String, Object>) stepsService.save(params);
        // Check return value
        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertEquals(stepId, ((Map) result).get("reference"));
    }

    @Test
    void testSave_WhenLocationIsNotNullAndExists() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("location", "Existing Location"); // Set the location parameter

        Location existingLocation = new Location();
        existingLocation.setId(1);
        existingLocation.setName("Existing Location");
        Location locationStart = new Location();
        locationStart.setId(2);
        Location locationEnd = new Location();
        locationEnd.setId(3);
        TransportMeans mode = new TransportMeans();
        mode.setId(4);
        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));
        when(locationRepository.findById(2)).thenReturn(Optional.of(locationStart));
        when(locationRepository.findById(3)).thenReturn(Optional.of(locationEnd));
        when(transportMeansRepository.findById(4)).thenReturn(Optional.of(mode));
        when(stepsRepository.save(any(Step.class))).thenAnswer(invocation -> {
            Step step = invocation.getArgument(0);
            step.setId(1); // Mock setting the ID after saving
            return step;
        });
        when(locationRepository.findByName("Existing Location")).thenReturn(existingLocation);

        Map<String, Object> result = (Map<String, Object>) stepsService.save(params);

        // Act
        System.out.println(result);
        assertNotNull(result);
        assertEquals(1, result.get("reference"));

    }

    @Test
    void testSave_WhenLocationIsNotNullAndDoesNotExist() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("location", "New Location");
        Location existingLocation = new Location();
        existingLocation.setId(1);
        existingLocation.setName("Existing Location");
        Location locationStart = new Location();
        locationStart.setId(2);
        Location locationEnd = new Location();
        locationEnd.setId(3);
        TransportMeans mode = new TransportMeans();
        mode.setId(4);
        Circuit circuit = new Circuit();
        circuit.setId(1);
        when(circuitRepository.findById(1)).thenReturn(Optional.of(circuit));
        when(locationRepository.findById(2)).thenReturn(Optional.of(locationStart));
        when(locationRepository.findById(3)).thenReturn(Optional.of(locationEnd));
        when(transportMeansRepository.findById(4)).thenReturn(Optional.of(mode));
        when(stepsRepository.save(any(Step.class))).thenAnswer(invocation -> {
            Step step = invocation.getArgument(0);
            step.setId(1); // Mock setting the ID after saving
            return step;
        });// Set the location parameter

        when(locationRepository.findByName("New Location")).thenReturn(null); // Simulate location not found

        // Act
        Map<String, Object> result = (Map<String, Object>) stepsService.save(params);
        assertNotNull(result);
        assertEquals(1, result.get("reference"));
        // Assert

        // You can also check that newLinksLs.getLocationStart() is set to the new Location
    }


    @Test
    void testDelete_WhenStepNotFoundInCircuit() {
        // Arrange
        Integer stepId = 1;
        when(stepsRepository.findById(stepId)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.ObjectNotFoundException exception =
                assertThrows(ExceptionControllerAdvice.ObjectNotFoundException.class,
                        () -> stepsService.delete(stepId));

        assertEquals("Steps not found", exception.getContent());
    }


    @Test
    void getStepByCircuit_whenCircuitExists_returnsMappedSteps() {
        // Arrange
        Integer circuitReference = 1;
        Circuit circuit = new Circuit(); // Assume circuit has minimal setup
        circuit.setId(circuitReference);

        Step step1 = new Step(); // Mock Step objects
        Step step2 = new Step();
        Object mappedStep1 = new Object(); // Mock mapped objects
        Object mappedStep2 = new Object();
        when(circuitRepository.findById(circuitReference)).thenReturn(Optional.of(circuit));
        when(stepsRepository.findByCircuit(circuit, Sort.by("orderStep"))).thenReturn(List.of(step1, step2));
        when(stepsMapper.apply(step1)).thenReturn(mappedStep1);
        when(stepsMapper.apply(step2)).thenReturn(mappedStep2);
        // Act
        List<Object> result = stepsService.getStepByCircuit(circuitReference);
        // Assert
        assertThat(result).containsExactly(mappedStep1, mappedStep2);
        verify(circuitRepository).findById(circuitReference);
        verify(stepsRepository).findByCircuit(circuit, Sort.by("orderStep"));
        verify(stepsMapper).apply(step1);
        verify(stepsMapper).apply(step2);
    }

    @Test
    void getStepByCircuit_whenCircuitNotFound_throwsException() {
        // Arrange
        Integer circuitReference = 1;
        when(circuitRepository.findById(circuitReference)).thenReturn(Optional.empty());

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception =
                assertThrows(ExceptionControllerAdvice.GeneralException.class,
                        () -> stepsService.getStepByCircuit(circuitReference));

        assertEquals("Circuit with ID " + circuitReference + " not found", exception.getContent());

        verify(circuitRepository).findById(circuitReference);
        verifyNoInteractions(stepsRepository, stepsMapper);
    }

    @Test
    void setOrderStepForNewStep_whenStepsInCircuitIsEmpty_setsOrderStepTo1() {
        // Arrange
        Step newStep = new Step();
        List<Step> stepsInCircuit = new ArrayList<>();

        // Act
        stepsService.setOrderStepForNewStep(newStep, stepsInCircuit, null);

        // Assert
        assertThat(newStep.getOrderStep()).isEqualTo(1);
        verifyNoInteractions(stepsRepository);
    }

    @Test
    void setOrderStepForNewStep_whenOrderStepIsNullOrLessThanOrEqualToZero_setsOrderStepToLastStepPlusOne() {
        // Arrange
        Step newStep = new Step();
        Step lastStep = new Step();
        lastStep.setOrderStep(2);
        List<Step> stepsInCircuit = List.of(lastStep);

        // Act
        stepsService.setOrderStepForNewStep(newStep, stepsInCircuit, null);

        // Assert
        assertThat(newStep.getOrderStep()).isEqualTo(3);
        verifyNoInteractions(stepsRepository);
    }

    @Test
    void setOrderStepForNewStep_whenOrderStepGreaterThanLastStep_setsOrderStepToLastStepPlusOne() {
        // Arrange
        Step newStep = new Step();
        Step lastStep = new Step();
        lastStep.setOrderStep(2);
        List<Step> stepsInCircuit = List.of(lastStep);

        // Act
        stepsService.setOrderStepForNewStep(newStep, stepsInCircuit, 5);

        // Assert
        assertThat(newStep.getOrderStep()).isEqualTo(3);
        verifyNoInteractions(stepsRepository);
    }

    @Test
    void setOrderStepForNewStep_whenOrderStepIsWithinRange_shiftsStepsAndSetsNewOrderStep() {
        // Arrange
        Step newStep = new Step();
        Step step1 = new Step();
        Step step2 = new Step();
        step1.setOrderStep(1);
        step2.setOrderStep(2);
        List<Step> stepsInCircuit = new ArrayList<>(List.of(step1, step2));

        // Act
        stepsService.setOrderStepForNewStep(newStep, stepsInCircuit, 1);

        // Assert
        assertThat(newStep.getOrderStep()).isEqualTo(1);
        assertThat(step1.getOrderStep()).isEqualTo(2);
        assertThat(step2.getOrderStep()).isEqualTo(3);
        verify(stepsRepository).saveAll(stepsInCircuit);
    }


    @Test
    void setOrderStepForUpdatedStep_whenStepOrderIsGreaterThanOrEqualToGivenOrderStep_incrementsOrderStep() {
        // Arrange
        Step step1 = new Step();
        step1.setOrderStep(1);

        Step step2 = new Step();
        step2.setOrderStep(2);

        Step step3 = new Step();
        step3.setOrderStep(3);

        List<Step> stepsInCircuit = new ArrayList<>(List.of(step1, step2, step3));
        Step steps = new Step();

        Integer newOrderStep = 2;

        // Act
        stepsService.setOrderStepForUpdatedStep(steps, stepsInCircuit, newOrderStep);

        // Assert
        assertThat(step1.getOrderStep()).isEqualTo(1); // Unchanged
        assertThat(step2.getOrderStep()).isEqualTo(3); // Incremented by 1
        assertThat(step3.getOrderStep()).isEqualTo(4); // Incremented by 1
        verify(stepsRepository).saveAll(stepsInCircuit);
        assertThat(steps.getOrderStep()).isEqualTo(newOrderStep);
    }

    @Test
    void get_whenStepExists_returnsMappedStep() {
        // Arrange
        Integer stepId = 1;
        Step step = new Step();
        step.setId(stepId);

        Object mappedStep = new Object(); // Example object to represent the mapped result

        when(stepsRepository.findById(stepId)).thenReturn(Optional.of(step));
        when(stepsMapper.apply(step)).thenReturn(mappedStep);

        // Act
        Object result = stepsService.get(stepId);

        // Assert
        assertThat(result).isEqualTo(mappedStep);
        verify(stepsRepository).findById(stepId);
        verify(stepsMapper).apply(step);
    }

    @Test
    void get_whenStepDoesNotExist_throwsException() {
        // Arrange
        Integer stepId = 1;
        when(stepsRepository.findById(stepId)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.ObjectNotFoundException exception =
                assertThrows(ExceptionControllerAdvice.ObjectNotFoundException.class,
                        () ->  stepsService.get(stepId));

        assertEquals("Steps not found", exception.getContent());
        verify(stepsRepository).findById(stepId);
        verifyNoMoreInteractions(stepsMapper);
    }

    @Test
    void getAll_returnsMappedListOfSteps() {
        // Arrange
        Step step1 = new Step();
        Step step2 = new Step();

        List<Step> steps = List.of(step1, step2);
        Object mappedStep1 = new Object();
        Object mappedStep2 = new Object();

        when(stepsRepository.findAll()).thenReturn(steps);
        when(stepsMapper.apply(step1)).thenReturn(mappedStep1);
        when(stepsMapper.apply(step2)).thenReturn(mappedStep2);

        // Act
        Object result = stepsService.getAll();

        // Assert
        assertThat(result).isInstanceOf(List.class);
        assertThat((List<Object>) result).containsExactly(mappedStep1, mappedStep2);
        verify(stepsRepository).findAll();
        verify(stepsMapper).apply(step1);
        verify(stepsMapper).apply(step2);
    }


    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getStepByCircuit() {
    }
}