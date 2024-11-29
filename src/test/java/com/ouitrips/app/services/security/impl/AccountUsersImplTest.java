package com.ouitrips.app.services.security.impl;

import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.UserAccountMapper;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.utils.DateUtil;
import com.ouitrips.app.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountUsersImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAccountMapper userAccountMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private DateUtil dateUtil;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private AccountUsersImpl accountUsersService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
         // assuming "USER" is a valid Role value

        // Populate with more fields as needed
    }

    @Test
    void testSaveUser_AddOperation_SuccessfulSave() {
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("username", "testUser");
        userAccountRequest.put("email", "test@example.com");
        userAccountRequest.put("roles", "ROLE_USER");
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword(bCryptPasswordEncoder.encode("testPassword"));
        user.setRoles("ROLE_USER");
        // Arrange
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(accountUsersService.savingUser(userAccountRequest)).thenReturn(user);

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = accountUsersService.saveUser(userAccountRequest, "add");
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());

    }

    @Test
    void testSaveUser_UPDATEOperation_SuccessfulSave() {
        User existingUser = new User();
        existingUser.setReference("existingUserRef");
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldEmail@example.com");
        existingUser.setPassword(bCryptPasswordEncoder.encode("oldPassword"));
        existingUser.setRoles("ROLE_USER");
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("userReference", "existingUserRef");
        userAccountRequest.put("username", "testUser");
        userAccountRequest.put("email", "test@example.com");
        userAccountRequest.put("roles", "ROLE_USER");
        User user = new User();
        user.setReference("existingUserRef");
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword(bCryptPasswordEncoder.encode("testPassword"));
        user.setRoles("ROLE_USER");

        // Arrange
        when(userRepository.findByReference("existingUserRef")).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newUsername")).thenReturn(false);
        when(userRepository.existsByEmail("newEmail@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(accountUsersService.updatingUser(userAccountRequest)).thenReturn(existingUser);

        // Act
        User result = accountUsersService.saveUser(userAccountRequest, "update");

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());


    }

    @Test
    void testSaveUser_SaveOperation_UsernameDuplicate() {
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("username", "newUsername");
        userAccountRequest.put("email", "test@example.com");

        when(userRepository.existsByUsername("newUsername")).thenReturn(true);

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> accountUsersService.saveUser(userAccountRequest, "add")
        );
        assertNotNull(exception);
        assertEquals(Map.of("username_error", true), exception.getContent());

    }
    @Test
    void testSaveUser_SaveOperation_EmailDuplicate() {
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("username", "newUsername");
        userAccountRequest.put("email", "test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> accountUsersService.saveUser(userAccountRequest, "add")
        );
        assertNotNull(exception);
        assertEquals(Map.of("email_error", true), exception.getContent());

    }

    @Test
    void testAddUser_SuccessfulAdd() {
        // Arrange
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("username", "newUsername");
        userAccountRequest.put("email", "test@example.com");
        userAccountRequest.put("roles", "ROLE_USER");

        User newUser = new User();
        newUser.setUsername("newUsername");
        newUser.setEmail("test@example.com");

        // Simulate the behavior of savingUser
        when(userRepository.existsByUsername("newUsername")).thenReturn(false); // Username is not taken
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false); // Email is not taken
        when(userRepository.save(any(User.class))).thenReturn(newUser); // Simulate saving the new user

        // Act
        User savedUser = accountUsersService.addUser(userAccountRequest);

        // Assert
        assertEquals("newUsername", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
    }
    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("userReference", "nonExistingUserRef");

        // Simulate that the user doesn't exist
        when(userRepository.findByReference("nonExistingUserRef")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> accountUsersService.updateUser(userAccountRequest));
    }
    @Test
    void testUpdateUser_UsernameDuplicate() {
        // Arrange
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("userReference", "existingUserRef");
        userAccountRequest.put("username", "existingUsername");
        userAccountRequest.put("email", "updated@example.com");

        User existingUser = new User();
        existingUser.setReference("existingUserRef");
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("old@example.com");

        // Simulate the behavior of finding the user and checking for duplicates
        when(userRepository.findByReference("existingUserRef")).thenReturn(Optional.of(existingUser));  // User exists
        when(userRepository.existsByUsername("existingUsername")).thenReturn(true);  // Username already taken

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> accountUsersService.updateUser(userAccountRequest)
        );

        assertEquals("username duplicated", exception.getContent());  // Assert the exception message
    }
    @Test
    void testUpdateUser_EmailDuplicate() {
        // Arrange
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("userReference", "existingUserRef");
        userAccountRequest.put("username", "updatedUsername");
        userAccountRequest.put("email", "existing@example.com");

        User existingUser = new User();
        existingUser.setReference("existingUserRef");
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("old@example.com");

        // Simulate the behavior of finding the user and checking for duplicates
        when(userRepository.findByReference("existingUserRef")).thenReturn(Optional.of(existingUser));  // User exists
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);  // Email already taken

        // Act & Assert
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> accountUsersService.updateUser(userAccountRequest)
        );

        assertEquals("email duplicated", exception.getContent());  // Assert the exception message
    }

    @Test
    void testDeleteUser_SuccessfulDeletion() {
        User existingUser = new User();
        existingUser.setReference("existingUserRef");
        existingUser.setUsername("existingUsername");
        existingUser.setEmail("existing@example.com");
        // Arrange
        String ref = "existingUserRef";

        // Simulate that the user exists in the repository
        when(userRepository.findByReference(ref)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertDoesNotThrow(() -> accountUsersService.deleteUser(ref));  // No exception should be thrown
    }

    @Test
    void testDeleteUser_UserNotFound() {

        // Arrange
        String ref = "nonExistingUserRef";

        // Simulate that the user does not exist
        when(userRepository.findByReference(ref)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> accountUsersService.deleteUser(ref));
    }

    @Test
    void testGetUser_SuccessfulFind() {
        User existingUser = new User();
        existingUser.setReference("existingUserRef");
        existingUser.setUsername("existingUsername");
        existingUser.setEmail("existing@example.com");
Object mappedUser = new Object();  // Replace with the actual expected object type

        // Arrange
        String ref = "existingUserRef";

        // Simulate that the user exists
        when(userRepository.findByReference(ref)).thenReturn(Optional.of(existingUser));

        // Simulate that the userAccountMapper maps the User to another object
        when(userAccountMapper.apply(existingUser)).thenReturn(mappedUser);

        // Act
        Object result = accountUsersService.getUser(ref);

        // Assert
        assertEquals(mappedUser, result);  // Assert that the correct mapped user is returned
    }

    @Test
    void testGetUser_UserNotFound() {
        // Arrange
        String ref = "nonExistingUserRef";

        // Simulate that the user does not exist
        when(userRepository.findByReference(ref)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> accountUsersService.getUser(ref));
    }


    @Test
    void testGetAllUsers_Successful() {
        User user1 = new User();
        user1.setReference("userRef1");
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

       User user2 = new User();
        user2.setReference("userRef2");
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        // Mock the mapped user objects
        Object mappedUser1 = mock(Object.class);  // Replace with the actual type expected from userAccountMapper
        Object mappedUser2 = mock(Object.class);
        // Arrange
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userAccountMapper.apply(user1)).thenReturn(mappedUser1);
        when(userAccountMapper.apply(user2)).thenReturn(mappedUser2);

        // Act
        List<Object> result = accountUsersService.getAllUsers();

        // Assert
        assertEquals(2, result.size());  // Check if two users are returned
        assertEquals(mappedUser1, result.get(0));  // Ensure first user is correctly mapped
        assertEquals(mappedUser2, result.get(1));  // Ensure second user is correctly mapped
    }

    @Test
    void testGetAllUsers_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<Object> result = accountUsersService.getAllUsers();

        // Assert
        assertEquals(0, result.size());  // Ensure the result is an empty list
    }
    @Mock
    private User mockUser;
    @Test
    void testRequestToUser_Success() {
        // Arrange: Prepare the mock user and the input data
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("roles", "ROLE_USER");
        userAccountRequest.put("firstName", "John");
        userAccountRequest.put("lastName", "Doe");
        userAccountRequest.put("email", "john.doe@example.com");
        userAccountRequest.put("username", "john_doe");
        userAccountRequest.put("birthDate", "1990-01-01");
        userAccountRequest.put("password", "password123");
        userAccountRequest.put("gsm", "123456789");
        userAccountRequest.put("mobilePhone", "987654321");

        // Stubbing behavior: mock the password encoder and date util
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(dateUtil.parseLocalDate(anyString(), anyString())).thenReturn(java.time.LocalDate.of(1990, 1, 1));

        // Act: Call the method under test
        accountUsersService.requestToUser(mockUser, userAccountRequest);

        // Assert: Verify that the mockUser object was updated with the expected values
        verify(mockUser).setRoles("[\"ROLE_USER\"]");
        verify(mockUser).setFirstName("John");
        verify(mockUser).setLastName("Doe");
        verify(mockUser).setEmail("john.doe@example.com");
        verify(mockUser).setUsername("john_doe");
        verify(mockUser).setBirthDate(java.time.LocalDate.of(1990, 1, 1));
        verify(mockUser).setPassword("encodedPassword");
        verify(mockUser).setGsm("123456789");
        verify(mockUser).setMobilePhone("987654321");

        // Additional verifications can be added as needed
    }
    @Test
    void testRequestToUser_InvalidRole() {
        // Arrange: Mock the static method UserUtils.isRoleValid to return false
        try (MockedStatic<UserUtils> mockedStatic = mockStatic(UserUtils.class)) {
            mockedStatic.when(() -> UserUtils.isRoleValid(anyString())).thenReturn(false); // Role is invalid

            // Prepare the input data with an invalid role
            Map<String, Object> userAccountRequest = new HashMap<>();
            userAccountRequest.put("roles", "INVALID_ROLE"); // Invalid role

            // Act & Assert: Verify that the exception is thrown
            ExceptionControllerAdvice.GeneralException thrown = assertThrows(
                    ExceptionControllerAdvice.GeneralException.class,
                    () -> accountUsersService.requestToUser(mockUser, userAccountRequest)
            );

            assertEquals("role not correct", thrown.getContent());
        }
    }



    }
