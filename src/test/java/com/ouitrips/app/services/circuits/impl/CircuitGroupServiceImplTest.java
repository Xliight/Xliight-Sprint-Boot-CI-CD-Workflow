package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.Category;
import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.CircuitGroupMapper;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.repositories.security.circuits.CategoryRepository;
import com.ouitrips.app.repositories.security.circuits.CircuitGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CircuitGroupServiceImplTest {
    @Mock
    private CircuitGroupRepository circuitGroupRepository;
    @Mock
    private CircuitGroupMapper circuitGroupMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CircuitGroupServiceImpl circuitGroupServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testSaveNewCircuitGroup() {
        // Prepare input parameters
        Map<String, Object> params = new HashMap<>();
        params.put("name", "New Circuit");
        params.put("description", "Description of new circuit");
        params.put("color", "Red");
        params.put("icon", "icon-path");
        params.put("userReference", 1);
        params.put("categoryReference", 2);
        // Mock behavior
        User mockUser = new User(); // Assume you have a User class
        Category mockCategory = new Category(); // Assume you have a Category class
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(mockCategory));
        CircuitGroup mockCircuitGroup = new CircuitGroup();
        mockCircuitGroup.setId(1); // Set ID for saved object
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(mockCircuitGroup);
        // Call the save method
        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;

        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));

    }

    @Test
    void testSave_UpdateExistingCircuitGroup() {
        // Arrange
        Integer circuitGroupId = 1;
        CircuitGroup existingCircuitGroup = new CircuitGroup();
        existingCircuitGroup.setId(circuitGroupId);
        existingCircuitGroup.setName("Old Name");
        existingCircuitGroup.setDescription("Old Description");
        existingCircuitGroup.setColor("Old Color");
        existingCircuitGroup.setIcon("Old Icon");
        Map<String, Object> params = new HashMap<>();
        params.put("id", circuitGroupId);
        params.put("name", "New Name");
        params.put("description", "New Description");
        params.put("color", "New Color");
        params.put("icon", "New Icon");
        params.put("userReference", 1);
        params.put("categoryReference", 1);
        // Mocking repository behavior
        when(circuitGroupRepository.findById(circuitGroupId)).thenReturn(Optional.of(existingCircuitGroup));
        User user = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Category category = new Category();
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        // Mock the save method to return the updated CircuitGroup
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenAnswer(invocation -> {
            CircuitGroup circuitGroupToSave = invocation.getArgument(0);
            circuitGroupToSave.setId(circuitGroupId);  // Ensure the ID is set on save
            return circuitGroupToSave; // Return the same object for verification
        });
        // Act
        Object result = circuitGroupServiceImpl.save(params);
        System.out.println(existingCircuitGroup.getName());
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(circuitGroupId, ((Map<?, ?>) result).get("reference"));
        // Verify that the fields are updated
        Assertions.assertEquals("New Name", existingCircuitGroup.getName());
        Assertions.assertEquals("New Description", existingCircuitGroup.getDescription());
        Assertions.assertEquals("New Color", existingCircuitGroup.getColor());
        Assertions.assertEquals("New Icon", existingCircuitGroup.getIcon());
        Assertions.assertEquals(user, existingCircuitGroup.getUser());
        Assertions.assertEquals(category, existingCircuitGroup.getCategory());
        // Verify the repository save method was called
        verify(circuitGroupRepository, times(1)).save(existingCircuitGroup);

    }
    @Test
    void delete() {
        Integer circuitGroupId = 1;
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(circuitGroupId);
        // Mocking the behavior of getById
        when(circuitGroupRepository.findById(circuitGroupId)).thenReturn(Optional.of(circuitGroup));
        // Act
        circuitGroupServiceImpl.delete(circuitGroupId);
        // Assert
        verify(circuitGroupRepository, times(1)).delete(circuitGroup);
    }

    @Test
    void get() {
        Integer circuitGroupId = 1;
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(circuitGroupId);
        circuitGroup.setName("Test Circuit Group");
        // Mocking the behavior of getById
        when(circuitGroupRepository.findById(circuitGroupId)).thenReturn(Optional.of(circuitGroup));
        when(circuitGroupMapper.apply(circuitGroup)).thenReturn("Mapped Circuit Group");
        // Act
        Object result = circuitGroupServiceImpl.get(circuitGroupId);
        // Assert
        assertNotNull(result);
        assertEquals("Mapped Circuit Group", result);
        verify(circuitGroupRepository, times(1)).findById(circuitGroupId);
    }

    @Test
    void getAll() {
        CircuitGroup circuitGroup1 = new CircuitGroup();
        circuitGroup1.setId(1);
        CircuitGroup circuitGroup2 = new CircuitGroup();
        circuitGroup2.setId(2);

        List<CircuitGroup> circuitGroups = Arrays.asList(circuitGroup1, circuitGroup2);

        // Mocking the behavior of findAll
        when(circuitGroupRepository.findAll()).thenReturn(circuitGroups);
        when(circuitGroupMapper.apply(circuitGroup1)).thenReturn("Mapped Circuit Group 1");
        when(circuitGroupMapper.apply(circuitGroup2)).thenReturn("Mapped Circuit Group 2");
        // Act
        List<Object> result = circuitGroupServiceImpl.getAll();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Mapped Circuit Group 1"));
        assertTrue(result.contains("Mapped Circuit Group 2"));
        verify(circuitGroupRepository, times(1)).findAll();
    }

    @Test
    void addDefaultByUser() {
        User user = new User();
        CircuitGroup defaultGroup = new CircuitGroup();
        defaultGroup.setName("Default Group");
        defaultGroup.setIsDefault(true);
        defaultGroup.setDescription("This is the default circuit group");
        defaultGroup.setColor("#000000");
        defaultGroup.setIcon("default-icon");
        defaultGroup.setUser(user);
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(defaultGroup);
        CircuitGroup result = circuitGroupServiceImpl.addDefaultByUser(user);
        assertNotNull(result);
        assertEquals("Default Group", result.getName());
        assertEquals("This is the default circuit group", result.getDescription());

    }
    @Test
    void getbyIdNotFound() {
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        when(circuitGroupRepository.findById(circuitGroup.getId())).thenReturn(Optional.empty());
        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> circuitGroupServiceImpl.getById(1)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("CircuitGroup not found", exception.getContent());
    }



    //---------
    @Test
    void save_whenNameIsProvided_setsName() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "New Circuit");
        CircuitGroup mockCircuitGroup = new CircuitGroup();
        mockCircuitGroup.setId(1);
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(mockCircuitGroup);
        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));
    }

    @Test
    void save_whenDescriptionIsProvided_setsDescription() {
        Map<String, Object> params = new HashMap<>();
        params.put("description", "Test Description");
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(circuitGroup);
        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));
    }

    @Test
    void save_whenColorIsProvided_setsColor() {
        Map<String, Object> params = new HashMap<>();
        params.put("color", "#FFFFF");
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(circuitGroup);

        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));
    }

    @Test
    void save_whenIconIsProvided_setsIcon() {
        Map<String, Object> params = new HashMap<>();
        params.put("icon", "dsfsdg");
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(circuitGroup);

        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));
    }

    @Test
    void save_whenUserReferenceIsProvided_setsUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("userReference", 1);
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(circuitGroup);
        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));
    }
    @Test
    void save_whenUserReferenceIsInvalid_throwsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("userReference", 1);

        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        when(circuitGroupRepository.findById(circuitGroup.getId())).thenReturn(Optional.of(circuitGroup));
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> circuitGroupServiceImpl.save(params)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("User with ID 1 not found", exception.getContent());
    }
    @Test
    void save_whenCategoryReferenceIsProvided_setsCategory() {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryReference", 1);
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        User user = new User();
        user.setId(1);
        Category category = new Category();
        category.setId(1);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(circuitGroupRepository.save(any(CircuitGroup.class))).thenReturn(circuitGroup);
        Object result = circuitGroupServiceImpl.save(params);
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNotNull(resultMap);
        assertEquals(1, resultMap.get("reference"));
    }

    @Test
    void save_whenCategoryReferenceIsInvalid_throwsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryReference", 1);
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> circuitGroupServiceImpl.save(params)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Category with ID 1 not found", exception.getContent());
    }
}