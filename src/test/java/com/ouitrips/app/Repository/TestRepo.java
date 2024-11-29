//package com.ouitrips.app.Repository;
//
//import com.ouitrips.app.entities.security.User;
//import com.ouitrips.app.repositories.security.UserRepository;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class TestRepo {
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//
//    }
//
//    @Test
//     void testFindUserByGsmPrefixe() {
//        // Create user1 and set required fields including username
//        User user1 = new User();
//        user1.setPrefixe("+1");
//        user1.setGsm("123456789");
//        user1.setEmail("qdfsd@gamil.com");
//        user1.setReference("user1");
//        user1.setPassword("qdfsd");
//        user1.setUsername("user1_username");  // Set username
//        userRepository.save(user1);
//
//        // Create user2 and set required fields including username
//        User user2 = new User();
//        user2.setPrefixe("+1");
//        user2.setGsm("987654321");
//        user2.setEmail("4@gamil.com");
//        user2.setReference("user2");
//        user2.setPassword("qdfsd");
//        user2.setUsername("user2_username");  // Set username
//        userRepository.save(user2);
//
//        // Create user3 and set required fields including username
//        User user3 = new User();
//        user3.setPrefixe("+44");
//        user3.setGsm("123456789");
//        user3.setEmail("5@gamil.com");
//        user3.setReference("user3");
//        user3.setPassword("qdfsd");
//        user3.setUsername("user3_username");  // Set username
//        userRepository.save(user3);
//
//        // Combine prefix + GSM for user1
//        String contact = "+1123456789";
//
//        // Call the repository method to find users by GSM prefix
//        List<User> users = userRepository.findUserByGsmPrefixe(contact);
//
//        // Check that the list contains exactly one user with the specified contact
//        assertThat(users).hasSize(1);
//        assertThat(users.get(0).getPrefixe()).isEqualTo("+1");
//        assertThat(users.get(0).getGsm()).isEqualTo("123456789");
//    }
//
//    @Test
//     void testFindUserByGsmPrefixeNoMatch() {
//        String contact = "+1123450000"; // Non-existing combination
//
//        List<User> users = userRepository.findUserByGsmPrefixe(contact);
//
//        // Check that the list is empty
//        assertThat(users).isEmpty();
//    }
//
//
//    @Test
//    void testGetAllUsers() {
//        // Given: Save some dummy users into the in-memory database
//        User user1 = new User();
//        user1.setUsername("john_doe");
//        user1.setEmail("john@example.com");
//        user1.setPassword("password");
//
//        User user2 = new User();
//        user2.setUsername("alice_smith");
//        user2.setEmail("alice@example.com");
//        user2.setPassword("password");
//
//        User user3 = new User();
//        user3.setUsername("bob_jones");
//        user3.setEmail("bob@example.com");
//        user3.setPassword("password");
//
//        // Save the users to the in-memory database
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//
//        // When: Call the getAllUsers method
//        List<User> users = userRepository.getAllUsers();
//
//        // Then: Verify the results
//        assertNotNull(users);  // Ensure the list is not null
//        assertEquals(4, users.size());  // Verify the expected number of users
//        assertEquals("alice_smith", users.get(1).getUsername());  // Verify sorting order
//        assertEquals("bob_jones", users.get(2).getUsername());
//        assertEquals("john_doe", users.get(3).getUsername());
//    }
//
//
//    @Test
//    void testGetUserByEmailOrUsername_WithEmail() {
//        User user1 = new User();
//        user1.setUsername("user1");
//        user1.setPassword("password");
//        user1.setEmail("user1@example.com");
//        user1.setReference("user1");
//
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("user2");
//        user2.setPassword("password");
//        user2.setEmail("user2@example.com");
//        user2.setReference("user2");
//        userRepository.save(user2);
//
//        User user3 = new User();
//        user3.setUsername("user3");
//        user3.setPassword("password");
//        user3.setEmail("user3@example.com");
//        user3.setReference("user3");
//        userRepository.save(user3);
//        // Find user by email
//        Optional<User> result = userRepository.getUserByEmailOrUsername("user1@example.com", null);
//
//        // Verify the result
//        assertTrue(result.isPresent());
//        assertEquals("user1", result.get().getUsername());
//    }
//
//    @Test
//    void testGetUserByEmailOrUsername_WithUsername() {
//        User user1 = new User();
//        user1.setUsername("user1");
//        user1.setPassword("password");
//        user1.setEmail("user1@example.com");
//        user1.setReference("user1");
//
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("user2");
//        user2.setPassword("password");
//        user2.setEmail("user2@example.com");
//        user2.setReference("user2");
//        userRepository.save(user2);
//        // Find user by username
//        Optional<User> result = userRepository.getUserByEmailOrUsername(null, "user2");
//
//        // Verify the result
//        assertTrue(result.isPresent());
//        assertEquals("user2@example.com", result.get().getEmail());
//    }
//
//    @Test
//    void testGetUserByEmailOrUsername_WithEmailAndUsername() {
//        User user1 = new User();
//        user1.setUsername("user1");
//        user1.setPassword("password");
//        user1.setEmail("user1@example.com");
//        user1.setReference("user1");
//
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("user2");
//        user2.setPassword("password");
//        user2.setEmail("user2@example.com");
//        user2.setReference("user2");
//        userRepository.save(user2);
//        // Find user by both email and username
//        Optional<User> result = userRepository.getUserByEmailOrUsername("user1@example.com", "user1");
//
//        // Verify the result
//        assertTrue(result.isPresent());
//        assertEquals("user1", result.get().getUsername());
//        assertEquals("user1@example.com", result.get().getEmail());
//    }
//
//    @Test
//    void testGetUserByEmailOrUsername_NoMatch() {
//        User user1 = new User();
//        user1.setUsername("user1");
//        user1.setPassword("password");
//        user1.setEmail("user1@example.com");
//        user1.setReference("user1");
//
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("user2");
//        user2.setPassword("password");
//        user2.setEmail("user2@example.com");
//        user2.setReference("user2");
//        userRepository.save(user2);
//        // Find user with non-matching email and username
//        Optional<User> result = userRepository.getUserByEmailOrUsername("nonexistent@example.com", "nonexistent");
//
//        // Verify that no user is found
//        assertFalse(result.isPresent());
//    }
//
//
//    @Autowired
//    private EntityManager entityManager;
//    private void createUserWithCreatedDate(Instant createdDate) {
//        User user = new User();
//        user.setEmail("test" + createdDate.toEpochMilli() + "@example.com");
//        user.setUsername("user" + createdDate.toEpochMilli());
//        user.setPassword("password");
//        user.setCreatedDate(createdDate);
//        userRepository.save(user);
//    }
//
//    @Test
//    void testCountNewAccounts_DailyInterval() {
//
//        // Create test users with specific creation dates
//        createUserWithCreatedDate(Instant.now().minus(1, ChronoUnit.DAYS));    // Created 1 day ago
//        createUserWithCreatedDate(Instant.now().minus(5, ChronoUnit.DAYS));    // Created 5 days ago
//
//        Date dateNow = new Date();
//        Integer daysInterval = 2;
//        String periodicite = "D";
//
//        Long count = userRepository.countNewAccounts(dateNow, daysInterval, periodicite);
//
//        // Expect 1 user created within the last 2 days
//        assertEquals(1, count);
//    }
//
//    @Test
//    void testCountNewAccounts_MonthlyInterval() {
//        createUserWithCreatedDate(Instant.now().minus(1, ChronoUnit.DAYS));    // Created 1 day ago
//        createUserWithCreatedDate(Instant.now().minus(5, ChronoUnit.DAYS));  // Created 1 day ago
//// Created 1 day ago
//        Date dateNow = new Date();
//        Integer daysInterval = 1;
//        String periodicite = "M";
//
//        Long count = userRepository.countNewAccounts(dateNow, daysInterval, periodicite);
//
//        // Expect 2 users created within the last month (1 day ago and 5 days ago)
//        assertEquals(2, count);
//    }
//
//    @Test
//    void testCountNewAccounts_YearlyInterval() {
//        createUserWithCreatedDate(Instant.now().minus(376, ChronoUnit.DAYS));    // Created 1 day ago
//   // Created 1 day ago
//
//        Date dateNow = new Date();
//        Integer daysInterval = 1;
//        String periodicite = "Y";
//
//        Long count = userRepository.countNewAccounts(dateNow, daysInterval, periodicite);
//
//        // Expect 4 users created within the last year
//        assertEquals(1, count);
//    }
//
//    @Test
//    void testCountNewAccounts_HourlyInterval() {
//        createUserWithCreatedDate(Instant.now().minus(0, ChronoUnit.DAYS));    // Created 1 day ago
//
//        Date dateNow = new Date();
//        Integer daysInterval = 24;
//        String periodicite = "H";
//
//        Long count = userRepository.countNewAccounts(dateNow, daysInterval, periodicite);
//
//        // Expect 1 user created within the last 24 hours
//        assertEquals(1, count);
//    }
//
//
//
//}
