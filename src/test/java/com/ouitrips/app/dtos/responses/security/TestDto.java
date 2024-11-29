package com.ouitrips.app.dtos.responses.security;

import com.ouitrips.app.dtos.agencydto.AgencyDTO;
import com.ouitrips.app.dtos.agencydto.DocumentDTO;
import com.ouitrips.app.dtos.circuitdto.CircuitDTO;
import com.ouitrips.app.dtos.circuitdto.StepDTO;
import com.ouitrips.app.enums.Roles;
import com.ouitrips.app.enums.RolesAuthority;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestDto {

    @Test
    void DtoSigninResponse() {

        SignInResponse.Data.Info info = new SignInResponse().new Data().new Info();
        info.setErrorPassword(true);
        info.setActive(true);
        info.setExist(true);
        info.setLifetime(365);
        info.setConnected(true);
        info.setLocked(false);


        // Creating instance of SignInResponse.Data.DataUser
        SignInResponse.Data.DataUser dataUser = new SignInResponse().new Data().new DataUser();
        dataUser.setAvatar("avatar_url.png");
        dataUser.setRoles(new ArrayList<String>() {{
            add("user");
            add("admin");
        }});
        dataUser.setToken("sample_token");
        dataUser.setFirstName("John");
        dataUser.setLastName("Doe");
        dataUser.setUsername("johndoe");
        dataUser.setEmail("johndoe@example.com");
        dataUser.setReference("ref123");

        // Creating instance of SignInResponse.Data
        SignInResponse.Data data = new SignInResponse().new Data();
        data.setInfo(info);
        data.setDataUser(dataUser);

        // Creating instance of SignInResponse
        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setStatus("success");
        signInResponse.setData(data);

        // Example usage: Getting values from SignInResponse
        signInResponse.getStatus();
        signInResponse.getData().getDataUser().getFirstName();
        signInResponse.getData().getDataUser().getEmail();
        signInResponse.getData().getInfo().isErrorPassword();
        signInResponse.getData().getDataUser().getRoles();

        SignInResponse anotherResponse = new SignInResponse();
        anotherResponse.setStatus("success");
        anotherResponse.setData(data);
        signInResponse.equals(anotherResponse);
       signInResponse.hashCode();
       anotherResponse.hashCode();
        signInResponse.toString();

    }
    @Test
    public void testEqualsAndCanEqual() {
        // Create instances of SignInResponse and nested classes
        SignInResponse response1 = new SignInResponse();
        SignInResponse response2 = new SignInResponse();

        SignInResponse.Data data1 = new SignInResponse().new Data();
        SignInResponse.Data data2 = new SignInResponse().new Data();
        SignInResponse.Data.DataUser dataUser1 = new SignInResponse().new Data().new DataUser();
        SignInResponse.Data.DataUser dataUser2 = new SignInResponse().new Data().new DataUser();

        // Set up fields for nested DataUser to be equal
        dataUser1.setAvatar("avatar");
        dataUser1.setUsername("user1");
        dataUser1.setEmail("user1@example.com");

        dataUser2.setAvatar("avatar");
        dataUser2.setUsername("user1");
        dataUser2.setEmail("user1@example.com");

        data1.setDataUser(dataUser1);
        data2.setDataUser(dataUser2);

        // Set up the SignInResponse to have same values for data field
        response1.setData(data1);
        response2.setData(data2);

        // Test equality of SignInResponse objects
        assertTrue(response1.equals(response2));  // Expect both to be equal

        // Ensure canEqual method works
        assertTrue(response1.canEqual(response2));  // Expect both to be able to equal each other

        // Modify fields and test for inequality
        dataUser2.setUsername("user2");
        response2.setData(data2);

        assertFalse(response1.equals(response2));  // Expect false when fields are different
    }

    @Test
    public void testEqualsAndCanEqualForData() {
        // Create instances of Data and nested classes
        SignInResponse.Data data1 = new SignInResponse().new Data();
        SignInResponse.Data data2 = new SignInResponse().new Data();
        SignInResponse.Data.Info info1 = new SignInResponse().new Data().new Info();
        SignInResponse.Data.Info info2 = new SignInResponse().new Data().new Info();

        // Set the values for Info to be equal
        info1.setActive(true);
        info2.setActive(true);

        data1.setInfo(info1);
        data2.setInfo(info2);

        // Test equality of Data objects
        assertTrue(data1.equals(data2));  // Expect both to be equal
        assertTrue(data1.canEqual(data2));  // Ensure canEqual works

        // Modify fields and test for inequality
        info2.setActive(false);
        data2.setInfo(info2);

        assertFalse(data1.equals(data2));  // Expect false when fields are different
    }

    @Test
    public void testEqualsAndCanEqualForDataUser() {
        // Create instances of DataUser
        SignInResponse.Data.DataUser dataUser1 = new SignInResponse().new Data().new DataUser();
        SignInResponse.Data.DataUser dataUser2 = new SignInResponse().new Data().new DataUser();

        // Set up DataUser fields to be equal
        dataUser1.setAvatar("avatar");
        dataUser1.setUsername("user1");
        dataUser1.setEmail("user1@example.com");

        dataUser2.setAvatar("avatar");
        dataUser2.setUsername("user1");
        dataUser2.setEmail("user1@example.com");

        // Test equality of DataUser objects
        assertTrue(dataUser1.equals(dataUser2));  // Expect both to be equal
        assertTrue(dataUser1.canEqual(dataUser2));  // Ensure canEqual works

        // Modify fields and test for inequality
        dataUser2.setUsername("user2");
        assertFalse(dataUser1.equals(dataUser2));  // Expect false when fields are different
    }

    @Test
    public void testEqualsAndCanEqualForInfo() {
        // Create instances of Info
        SignInResponse.Data.Info info1 = new SignInResponse().new Data().new Info();
        SignInResponse.Data.Info info2 = new SignInResponse().new Data().new Info();

        // Set Info fields to be equal
        info1.setActive(true);
        info2.setActive(true);

        // Test equality of Info objects
        assertTrue(info1.equals(info2));  // Expect both to be equal
        assertTrue(info1.canEqual(info2));  // Ensure canEqual works

        // Modify fields and test for inequality
        info2.setActive(false);
        assertFalse(info1.equals(info2));  // Expect false when fields are different
    }

    @Test
    void CircuitDto(){
        CircuitDTO circuit = new CircuitDTO(
                1,                                        // ID
                "Mountain Adventure",                     // Name
                150.5,                                    // Distance
                false,                                    // isDeleted
                LocalDate.of(2024, 5, 1),                 // dateDebut (Start Date)
                LocalDate.of(2024, 5, 15),                // dateFin (End Date)
                10,                                       // circuitGroupId
                Arrays.asList(101, 102, 103),             // agencyCircuitIds
                Arrays.asList(201, 202, 203)              // stepIds
        );
        circuit.setName("Ocean Expedition");
        circuit.setDistance(200.75);
        circuit.setIsDeleted(true);
        circuit.setDateDebut(LocalDate.of(2024, 6, 1));
        circuit.setDateFin(LocalDate.of(2024, 6, 15));
        circuit.setCircuitGroupId(20);
        circuit.setAgencyCircuitIds(Arrays.asList(104, 105));
        circuit.setStepIds(Arrays.asList(204, 205));
        circuit.getId();
        circuit.getName();
        circuit.getDistance();
        circuit.getIsDeleted();
        circuit.getDateDebut();
        circuit.getDateFin();
        circuit.getCircuitGroupId();
        circuit.getAgencyCircuitIds();
        circuit.getStepIds();

        StepDTO step = new StepDTO(
                1,                         // ID
                "Start Point",              // Name
                "Go straight for 5 miles",  // Directions
                1,                         // Order Step
                true                        // State (active)
        );

        step.getId();
        step.getName();
        step.getDirections();
        step.getOrderStep();
        step.getState();
        step.setName("Finish Line");
        step.setDirections("Turn left after 3 miles");
        step.setOrderStep(2);
        step.setState(false);


        AgencyDTO agency = new AgencyDTO(
                1,                        // ID
                "Best Travel Agency",     // Name
                "1234 Main St, Cityville",// Address
                "+1234567890",            // Contact Number
                "contact@bestagency.com", // Email
                "Leading agency offering best travel packages", // Description
                1001,                     // Owner ID
                false                      // isDeleted (active)
        );

        // Print initial values using getter methods
        agency.getId();
        agency.getName();
        agency.getAddress();
        agency.getContactNumber();
        agency.getEmail();
        agency.getDescription();
        agency.getOwnerId();
        agency.getIsDeleted();

        agency.setName("Elite Travel Agency");
        agency.setContactNumber("+1987654321");
        agency.setEmail("support@elitetravel.com");
        agency.setIsDeleted(true);

        byte[] documentFile = new byte[]{1, 2, 3, 4}; // Just a placeholder for the file (normally this would be a file content in bytes)

        DocumentDTO document = new DocumentDTO(
                1,                                // ID
                "Travel Itinerary",               // Title
                "PDF",                            // Type
                "/documents/itinerary.pdf",       // File Path
                documentFile,                     // File in bytes
                101                               // Agency ID
        );

        // Print initial values using getter methods
        document.getId();
        document.getTitle();
        document.getType();
        document.getFilePath();
        document.getAgencyId();

        // Print file content (or size) to confirm file is set
        document.getFile();

        // Modify values using setter methods
        document.setTitle("Updated Travel Itinerary");
        document.setFilePath("/documents/updated_itinerary.pdf");
        document.setId(1);
        document.setType("efdsq");
        document.setAgencyId(2);
        document.setFile(documentFile);

    }

    @Test
    void testAllArgsConstructor() {
        Roles roles = new Roles("Admin", 30);
        assertEquals("Admin", roles.getName());
        assertEquals(30, roles.getAge());
    }

    @Test
    void testNoArgsConstructor() {
        Roles roles = new Roles();
        assertNull(roles.getName());
        assertNull(roles.getAge());
    }

    @Test
    void testSettersAndGetters() {
        Roles roles = new Roles();
        roles.setName("User");
        roles.setAge(25);

        assertEquals("User", roles.getName());
        assertEquals(25, roles.getAge());
    }

    @Test
    void testEmbeddableAnnotation() {
        Roles roles = new Roles("Admin", 30);
        assertNotNull(roles);
    }

    @Test
    void testEqualsAndHashCode() {
        Roles roles1 = new Roles("Admin", 30);
        Roles roles2 = new Roles("Admin", 30);
        Roles roles3 = new Roles("User", 25);

        // Test equality
        assertEquals(roles1, roles2); // Same content should be equal
        assertNotEquals(roles1, roles3); // Different content should not be equal

        // Test hashCode
        assertEquals(roles1.hashCode(), roles2.hashCode()); // Same content should have same hashCode
        assertNotEquals(roles1.hashCode(), roles3.hashCode()); // Different content should have different hashCode
    }

    @Test
    void testUtilityClassConstructor() {
        // Ensure that the constructor throws an IllegalStateException
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            // Try to instantiate the utility class (which should not be allowed)
            new RolesAuthority();
        });
        assertEquals("Utility class", exception.getMessage());
    }

//    @Test
//    void testUserAuthorities() {
//        // Ensure the USER_AUTHORITIES array contains the correct authority
//        String[] userAuthorities = RolesAuthority.USER_AUTHORITIES.toArray(new String[0]);
//        assertArrayEquals(new String[] { "user:read" }, userAuthorities);
//    }
//
//    @Test
//    void testHrAuthorities() {
//        // Ensure the HR_AUTHORITIES array contains the correct authorities
//        String[] hrAuthorities = RolesAuthority.HR_AUTHORITIES;
//        assertArrayEquals(new String[] { "user:read", "user:update" }, hrAuthorities);
//    }
//
//    @Test
//    void testManagerAuthorities() {
//        // Ensure the MANAGER_AUTHORITIES array contains the correct authorities
//        String[] managerAuthorities = RolesAuthority.MANAGER_AUTHORITIES;
//        assertArrayEquals(new String[] { "user:read", "user:update" }, managerAuthorities);
//    }
//
//    @Test
//    void testAdminAuthorities() {
//        // Ensure the ADMIN_AUTHORITIES array contains the correct authorities
//        String[] adminAuthorities = RolesAuthority.ADMIN_AUTHORITIES;
//        assertArrayEquals(new String[] { "user:read", "user:create", "user:update" }, adminAuthorities);
//    }
//
//    @Test
//    void testSuperAdminAuthorities() {
//        // Ensure the SUPER_ADMIN_AUTHORITIES array contains the correct authorities
//        String[] superAdminAuthorities = RolesAuthority.SUPER_ADMIN_AUTHORITIES;
//        assertArrayEquals(new String[] { "user:read", "user:create", "user:update", "user:delete" }, superAdminAuthorities);
//    }


}

