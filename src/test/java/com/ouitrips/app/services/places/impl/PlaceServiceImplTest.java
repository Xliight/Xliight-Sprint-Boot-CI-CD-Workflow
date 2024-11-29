package com.ouitrips.app.services.places.impl;

import com.ouitrips.app.entities.places.*;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.places.PlaceMapper;
import com.ouitrips.app.osrmservice.Table;
import com.ouitrips.app.repositories.security.places.*;
import com.ouitrips.app.services.places.TSPService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.time.LocalTime;
import java.util.*;

import java.util.Map;


class PlaceServiceImplTest {
    @Mock
    private PlaceRepository placeRepository;
    @InjectMocks
    private PlaceServiceImpl placeService;

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private PlaceInfoRepository placeInfoRepository;

    @Mock
    private PlaceDurationRepository placeDurationRepository;

    @Mock
    private PlacePricingRepository placePricingRepository;

    @Mock
    private PlaceOpeningHourRepository placeOpeningHourRepository;

    @Mock
    private PlaceTimeZoneRepository placeTimeZoneRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private LinksPtRepository linksPtRepository;

    @Mock
    private PlaceTypeRepository placeTypeRepository;

    @Mock
    private LinksTpRepository linksTpRepository;

    @Mock
    private PlaceTagRepository placeTagRepository;

    @Mock
    private Table tableService;

    @Mock
    private TSPService tspService;

    @Mock
    private CitySummaryRepository citySummaryRepository;

    private Map<String, Object> params;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
     void testSaveNewPlace() {
        Place newPlace = new Place();
        newPlace.setId(1);
        params = new HashMap<>();

        params.put("title", "Test Place");
        params.put("latitude", 34.0522);
        params.put("longitude", -118.2437);
        params.put("description", "A test description");
        params.put("content", "A test content");
        params.put("address", "123 Test St");
        params.put("popularity", "high");
        params.put("timeZoneName", "America/Los_Angeles");
        params.put("cityReference", 1);
        params.put("placeTypeCode", "TYPE1");
        params.put("placeTag", "TAG1");

        when(placeRepository.save(any(Place.class))).thenReturn(newPlace);
        // Call the save method and assert that it returns a non-null result
         Object j=placeService.save(params);
         assertNotNull(j);
        assertEquals(1, ((Map) j).get("reference"));
    }

    @Test
     void testUpdatePlace() {
        Map<String,Object> paramss = new HashMap<>();
        paramss.put("id", 1);
        paramss.put("title", "Updated Central Park");
        paramss.put("address", "Updated Address, NY");
        paramss.put("city", "New York");
        paramss.put("country", "USA");
        paramss.put("latitude", 40.785091);
        paramss.put("longitude", -73.968285);
        paramss.put("description", "Updated description of the park.");

        // Mock an existing Place object to be updated
        Place existingPlace = new Place();
        City city=new City();
        existingPlace.setId(1);
        existingPlace.setTitle("Central Park");
        existingPlace.setAddress("New York, NY");
        existingPlace.setCity(city);
        existingPlace.setContent("USA");
        existingPlace.setLatitude(40.785091);
        existingPlace.setLongitude(-73.968285);
        existingPlace.setPlaceInfos(new HashSet<>()); // Ensure it's initialized
        existingPlace.setPlaceDurations(new HashSet<>());
        existingPlace.setPlaceTags(new HashSet<>());
        existingPlace.setPlacePricing(new HashSet<>());
        existingPlace.setPlaceTimeZone(null);
        existingPlace.setPlaceOpeningHours(new HashSet<>());
        existingPlace.setDescription("A large  park in New York City.");
        // Mock the repository to return the existing Place when searching by ID
        when(placeRepository.findById(1)).thenReturn(Optional.of(existingPlace));
        // Mock the save method in the repository to return the updated Place object
        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);
        // Call the update method in PlaceServiceImpl
        Object result = placeService.save( paramss);
        assertNotNull(result);
        assertEquals(1, ((Map) result).get("reference"));
        verify(placeRepository, times(1)).save(any(Place.class));

        // Verify that result is not null and is the expected object
    }

    @Test
     void testSave_WhenLinksPtDoesNotExist_CreatesAndSavesNewLinksPt() {
        Place place = new Place();
        place.setId(1); // Setting a dummy ID
        PlaceType placeType = new PlaceType();
        placeType.setId(1);
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Test Place");
        params.put("latitude", 12.34);
        params.put("longitude", 56.78);
        params.put("placeTypeCode", "TEST_TYPE");

        when(placeRepository.save(any(Place.class))).thenReturn(place);
        when(placeTypeRepository.findByCode("TEST_TYPE")).thenReturn(placeType);
        when(linksPtRepository.existsById(any(LinksPtId.class))).thenReturn(false);
        when(linksPtRepository.findByPlace(any(Place.class))).thenReturn(null);

        // Act
        Object result = placeService.save(params);

        // Assert
        verify(linksPtRepository).save(any(LinksPt.class)); // Verify save was called
        // Optionally, you can assert that the returned reference matches the expected ID
        assertEquals(place.getId(), ((Map<?, ?>) result).get("reference"));
    }

    @Test
     void testSave_WhenLinksPtExists_DoesNotCreateNewLinksPt() {
        Place place = new Place();
        place.setId(1); // Setting a dummy ID
        PlaceType placeType = new PlaceType();
        placeType.setId(1);
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Test Place");
        params.put("latitude", 12.34);
        params.put("longitude", 56.78);
        params.put("placeTypeCode", "TEST_TYPE");

        LinksPt existingLinksPt = new LinksPt();
        existingLinksPt.setPlace(place);
        existingLinksPt.setPlaceType(placeType);

        when(placeRepository.save(any(Place.class))).thenReturn(place);
        when(placeTypeRepository.findByCode("TEST_TYPE")).thenReturn(placeType);
        when(linksPtRepository.existsById(any(LinksPtId.class))).thenReturn(true);
        when(linksPtRepository.findByPlace(place)).thenReturn(existingLinksPt);

        // Act
        Object result = placeService.save(params);

        // Assert
        verify(linksPtRepository, never()).save(any(LinksPt.class)); // Verify save was not called
        assertEquals(place.getId(), ((Map<?, ?>) result).get("reference"));
    }

    @Test
     void testSave_WhenLinksTpDoesNotExist_CreatesAndSavesNewLinksTp() {
      Place  place = new Place();
        place.setId(1); // Setting a dummy ID
      PlaceTag  placeTag = new PlaceTag();
        placeTag.setId(1);
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Test Place");
        params.put("latitude", 12.34);
        params.put("longitude", 56.78);
        params.put("placeTag", "TEST_TAG");

        when(placeRepository.save(any(Place.class))).thenReturn(place);
        when(placeTagRepository.findByTag("TEST_TAG")).thenReturn(placeTag);
        when(linksTpRepository.existsById(any(LinksTpId.class))).thenReturn(false);
        when(linksTpRepository.findByPlaceAndPlaceTag(place, placeTag)).thenReturn(null);

        // Act
        Object result = placeService.save(params);

        // Assert
        verify(linksTpRepository).save(any(LinksTp.class)); // Verify save was called
        // Optionally, you can assert that the returned reference matches the expected ID
        assertEquals(place.getId(), ((Map<?, ?>) result).get("reference"));
    }

    @Test
     void testSave_WhenLinksTpExists_DoesNotCreateNewLinksTp() {
        Place  place = new Place();
        place.setId(1); // Setting a dummy ID
        PlaceTag  placeTag = new PlaceTag();
        placeTag.setId(1);
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Test Place");
        params.put("latitude", 12.34);
        params.put("longitude", 56.78);
        params.put("placeTag", "TEST_TAG");

        LinksTp existingLinksTp = new LinksTp();
        existingLinksTp.setPlace(place);
        existingLinksTp.setPlaceTag(placeTag);

        when(placeRepository.save(any(Place.class))).thenReturn(place);
        when(placeTagRepository.findByTag("TEST_TAG")).thenReturn(placeTag);
        when(linksTpRepository.existsById(any(LinksTpId.class))).thenReturn(true);
        when(linksTpRepository.findByPlaceAndPlaceTag(place, placeTag)).thenReturn(existingLinksTp);

        // Act
        Object result = placeService.save(params);

        // Assert
        verify(linksTpRepository, never()).save(any(LinksTp.class)); // Verify save was not called
        assertEquals(place.getId(), ((Map<?, ?>) result).get("reference"));
    }


    @Test
     void testSave_NewPlace_Success() throws ExceptionControllerAdvice.GeneralException {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("title", "New Place");
        params.put("description", "Description of the new place.");
        params.put("content", "Content of the new place.");
        params.put("latitude", 12.34);
        params.put("longitude", 56.78);
        params.put("address", "123 Place St.");
        params.put("popularity", "High");
        params.put("timeZoneName", "UTC");
        params.put("cityReference", 1);
        params.put("placeTypeCode", "TYPE1");
        params.put("placeTag", "TAG1");
        params.put("duration", 120L);
        params.put("price_min", 10.0);
        params.put("price_max", 50.0);
        params.put("price", 30.0);
        params.put("opening_time", LocalTime.of(9, 0));
        params.put("close_time", LocalTime.of(18, 0));
        params.put("contentPlaceInfo", "Info content.");
        params.put("addressPlaceInfo", "Address info.");
        params.put("phone", "123-456-7890");
        params.put("phone2", "098-765-4321");
        params.put("website", "http://example.com");
        Place p = new Place();
        p.setId(1);
        when(cityRepository.findById(1)).thenReturn(Optional.of(new City()));
        when(placeTypeRepository.findByCode("TYPE1")).thenReturn(new PlaceType());
        when(placeTagRepository.findByTag("TAG1")).thenReturn(new PlaceTag());
        when(placeRepository.save(any(Place.class))).thenReturn(p);

        // Act
        Object result = placeService.save(params);

        // Assert
        assertNotNull(result);
        assertEquals(p.getId(), ((Map<?, ?>) result).get("reference"));

    }



    @Test
    void testSave_ExistingPlace_RetrievesExistingEntities() throws ExceptionControllerAdvice.GeneralException {
        // Given
        Place place = new Place();
        place.setId(1);
        place.setPlaceInfos(new HashSet<>());
        place.setPlaceDurations(new HashSet<>());
        place.setPlacePricing(new HashSet<>());
        place.setPlaceOpeningHours(new HashSet<>());
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        when(placeRepository.findById(1)).thenReturn(Optional.of(place));

        PlaceInfo existingPlaceInfo = new PlaceInfo();
        place.getPlaceInfos().add(existingPlaceInfo);
        PlaceDuration existingPlaceDuration = new PlaceDuration();
        place.getPlaceDurations().add(existingPlaceDuration);
        PlacePricing existingPlacePricing = new PlacePricing();
        place.getPlacePricing().add(existingPlacePricing);
        PlaceOpeningHour existingPlaceOpeningHour = new PlaceOpeningHour();
        place.getPlaceOpeningHours().add(existingPlaceOpeningHour);
        when(placeRepository.save(any(Place.class))).thenReturn(place);
        // When
        Object result = placeService.save(params);

        // Then
        assertNotNull(result);
        verify(placeInfoRepository, times(1)).save(existingPlaceInfo);
        verify(placeDurationRepository, times(1)).save(existingPlaceDuration);
        verify(placePricingRepository, times(1)).save(existingPlacePricing);
        verify(placeOpeningHourRepository, times(1)).save(existingPlaceOpeningHour);
    }

    @Test
    void testSave_ThrowsException_WhenTitleLatitudeLongitudeAreMissing() {
        // Given
        Map<String, Object> param = new HashMap<>();


        // When & Then
        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> placeService.save(param)
        );
        assertEquals("Title, latitude, and longitude are required for creating a new place.", exception.getContent());
    }

    @Test
    void testSave_SetsPlaceTimeZone_WhenProvided() throws ExceptionControllerAdvice.GeneralException {
        // Given
        Place place = new Place();
        place.setId(1);
        place.setPlaceInfos(new HashSet<>());
        place.setPlaceDurations(new HashSet<>());
        place.setPlacePricing(new HashSet<>());
        place.setPlaceOpeningHours(new HashSet<>());
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Test Place");
        params.put("latitude", 12.34);
        params.put("longitude", 56.78);
        params.put("timeZoneName", "GMT+0");
        PlaceTimeZone placeTimeZone = new PlaceTimeZone();

        when(placeTimeZoneRepository.findByName("GMT+0")).thenReturn(placeTimeZone);
        when(placeRepository.save(any(Place.class))).thenReturn(place);

        // When
        Object result=placeService.save(params);

        assertEquals(place.getId(), ((Map<?, ?>) result).get("reference"));
    }


    @Test
     void testDelete_WhenIdExists() {
        // Initialize a mock Place entity
        Place place = new Place();
        place.setId(1);

        // Mock the repository behavior for findById and delete
        when(placeRepository.findById(1)).thenReturn(Optional.of(place));
        // Act
        placeService.delete(1);

        // Assert
        verify(placeRepository).findById(1);  // Ensure it checks for the Place by ID
        verify(placeRepository).delete(place); // Ensure it deletes the correct Place
    }

    @Test
     void testDelete_WhenIdDoesNotExist() {
        // Arrange: return empty Optional to simulate Place not found
        when(placeRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            placeService.delete(2);
        } catch (Exception e) {
            // Expected behavior: log or handle exception if necessary
        }

        // Verify: delete should not be called if Place is not found
        verify(placeRepository, never()).delete(any(Place.class));
    }

    @Test
     void testGet_WhenIdExists() {
        // Act
        Place place = new Place();
        place.setId(1);

        // Mock repository and mapper behavior for getById and apply
        when(placeRepository.findById(1)).thenReturn(Optional.of(place));
        when(placeMapper.apply(place)).thenReturn(Map.of("id", place.getId()));
        Object result = placeService.get(1);

        // Assert
        verify(placeRepository).findById(1);
        verify(placeMapper).apply(place);
        assertEquals(Map.of("id", place.getId()), result);
    }
    @Test
     void testGet_WhenIdDoesNotExist() {
        // Arrange
        when(placeRepository.findById(2)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> placeService.get(2)
        );
        assertEquals("Place not found", exception.getContent());
        verify(placeRepository).findById(2);
        verify(placeMapper, never()).apply(any());
    }

    @Test
     void testGetAll() {
        // Arrange
        Place place1 = new Place();
        place1.setId(1);
        Place place2 = new Place();
        place2.setId(2);

        when(placeRepository.findAll()).thenReturn(Arrays.asList(place1, place2));
        when(placeMapper.apply(place1)).thenReturn(Map.of("id", place1.getId()));
        when(placeMapper.apply(place2)).thenReturn(Map.of("id", place2.getId()));

        // Act
        List<Object> result = (List<Object>) placeService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(Map.of("id", 1)));
        assertTrue(result.contains(Map.of("id", 2)));
        verify(placeRepository).findAll();
        verify(placeMapper).apply(place1);
        verify(placeMapper).apply(place2);
    }

    @Test
     void testGetById_WhenIdExists() {
        // Arrange
        Place place = new Place();
        place.setId(1);

        when(placeRepository.findById(1)).thenReturn(Optional.of(place));

        // Act
        Place result = placeService.getById(1);

        // Assert
        assertEquals(1, result.getId());
        verify(placeRepository).findById(1);
    }

    @Test
     void testGetById_WhenIdDoesNotExist() {
        // Arrange
        when(placeRepository.findById(2)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () -> placeService.getById(2)
        );
        assertEquals("Place not found", exception.getContent());
        verify(placeRepository).findById(2);
    }


    @Test
     void testUpdateCitySummaries_CityExists_CitySummaryExists() {
        // Mock input data
        List<String> citiesVisited = Collections.singletonList("CityA");

        // Mock city and city summary
        City city = new City();
        city.setId(1);
        city.setName("CityA");

        CitySummary citySummary = new CitySummary();
        citySummary.setCity(city);
        Place place1 = new Place();
        Place place2 = new Place();
        List<Place> placesInCity = Arrays.asList(place1, place2);
        PlaceDuration placeDuration1 = new PlaceDuration();
        placeDuration1.setDuration(5400L); // 1.5 hours
        PlaceDuration placeDuration2 = new PlaceDuration();
        placeDuration2.setDuration(5400L); // 1.5 hours // 6 hours

        when(cityRepository.findByName("CityA")).thenReturn(city);
        when(citySummaryRepository.findByCity(city)).thenReturn(Optional.of(citySummary));
        when(placeRepository.findByCityId(city.getId())).thenReturn(placesInCity);
        when(placeDurationRepository.findByPlace(place1)).thenReturn(Optional.of(placeDuration1));
        when(placeDurationRepository.findByPlace(place2)).thenReturn(Optional.of(placeDuration2));

        // Execute method
        placeService.updateCitySummaries(citiesVisited);

        // Verify CitySummary updates
        verify(citySummaryRepository, times(1)).save(citySummary);
        assertEquals(2, citySummary.getTotalPlaces());
        assertEquals(1, citySummary.getTotalDuration()); // (10800 + 21600) / 10800 = 1 day
    }

    @Test
     void testUpdateCitySummaries_CityExists_NoCitySummary() {
        // Mock input data
        List<String> citiesVisited = Collections.singletonList("CityB");

        // Mock city without an existing summary
        City city = new City();
        city.setId(2);
        city.setName("CityB");

        Place place1 = new Place();
        List<Place> placesInCity = Collections.singletonList(place1);

        PlaceDuration placeDuration1 = new PlaceDuration();
        placeDuration1.setDuration(86400L); // 1 day

        // Set up mocks
        when(cityRepository.findByName("CityB")).thenReturn(city);
        when(citySummaryRepository.findByCity(city)).thenReturn(Optional.empty());
        when(placeRepository.findByCityId(city.getId())).thenReturn(placesInCity);
        when(placeDurationRepository.findByPlace(place1)).thenReturn(Optional.of(placeDuration1));

        // Execute method
        placeService.updateCitySummaries(citiesVisited);

        // Verify CitySummary creation and save
        verify(citySummaryRepository, times(1)).save(any(CitySummary.class));
    }

    @Test
     void testUpdateCitySummaries_CityNotFound() {
        // Mock input data
        List<String> citiesVisited = Collections.singletonList("NonExistentCity");

        // Set up mocks
        when(cityRepository.findByName("NonExistentCity")).thenReturn(null);

        // Execute method
        placeService.updateCitySummaries(citiesVisited);

        // Verify no save is called and "City not found" message is printed
        verify(citySummaryRepository, never()).save(any());
    }

    @Test
     void testGeneratePlans_BaseCaseZeroRemainingDays() {
        int remainingDays = 0;
        String[] cities = {"CityA", "CityB"};
        List<Integer> minDaysPerCity = Arrays.asList(2, 3);
        int[] maxDaysPerCity = {2, 3};
        List<Map<String, Integer>> allPlans = new ArrayList<>();

        List<Map<String, Integer>> result = placeService.generatePlans(remainingDays, cities, minDaysPerCity, maxDaysPerCity, allPlans);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).get("CityA").intValue());
        assertEquals(3, result.get(0).get("CityB").intValue());
    }

    @Test
     void testGeneratePlans_DistributeRemainingDays() {
        int remainingDays = 2;
        String[] cities = {"CityA", "CityB"};
        List<Integer> minDaysPerCity = Arrays.asList(0, 0);
        int[] maxDaysPerCity = {2, 2};
        List<Map<String, Integer>> allPlans = new ArrayList<>();

        List<Map<String, Integer>> result = placeService.generatePlans(remainingDays, cities, minDaysPerCity, maxDaysPerCity, allPlans);

        assertFalse(result.isEmpty());
        for (Map<String, Integer> plan : result) {
            int totalDays = plan.values().stream().mapToInt(Integer::intValue).sum();
            assertEquals(2, totalDays); // Ensure total days match remainingDays
            assertTrue(plan.get("CityA") <= maxDaysPerCity[0]);
            assertTrue(plan.get("CityB") <= maxDaysPerCity[1]);
        }
    }

    @Test
     void testGeneratePlans_ExceedsMaxDays() {
        int remainingDays = 5;
        String[] cities = {"CityA", "CityB"};
        List<Integer> minDaysPerCity = Arrays.asList(0, 0);
        int[] maxDaysPerCity = {2, 3};
        List<Map<String, Integer>> allPlans = new ArrayList<>();

        List<Map<String, Integer>> result = placeService.generatePlans(remainingDays, cities, minDaysPerCity, maxDaysPerCity, allPlans);

        for (Map<String, Integer> plan : result) {
            assertTrue(plan.get("CityA") <= maxDaysPerCity[0]);
            assertTrue(plan.get("CityB") <= maxDaysPerCity[1]);
        }
    }

    @Test
     void testGeneratePlans_InvalidPlan() {
        int remainingDays = 0;
        String[] cities = {"CityA", "CityB"};
        List<Integer> minDaysPerCity = Arrays.asList(3, 4); // Exceeds maxDaysPerCity
        int[] maxDaysPerCity = {2, 3};
        List<Map<String, Integer>> allPlans = new ArrayList<>();

        List<Map<String, Integer>> result = placeService.generatePlans(remainingDays, cities, minDaysPerCity, maxDaysPerCity, allPlans);

        assertTrue(result.isEmpty()); // No valid plans should be generated
    }

    @Test
     void testExtractDistanceMatrix() throws JSONException {
        // Create a mock JSON response representing a distance matrix
        JSONObject distanceMatrixResponse = new JSONObject();
        JSONArray distances = new JSONArray();

        // Constructing a 3x3 matrix as a sample
        distances.put(new JSONArray(Arrays.asList(0.0, 1.5, 2.3)));
        distances.put(new JSONArray(Arrays.asList(1.5, 0.0, 3.8)));
        distances.put(new JSONArray(Arrays.asList(2.3, 3.8, 0.0)));

        distanceMatrixResponse.put("distances", distances);

        // Expected output matrix
        List<List<Double>> expectedMatrix = Arrays.asList(
                Arrays.asList(0.0, 1.5, 2.3),
                Arrays.asList(1.5, 0.0, 3.8),
                Arrays.asList(2.3, 3.8, 0.0)
        );

        // Invoke the method
        List<List<Double>> resultMatrix = placeService.extractDistanceMatrix(distanceMatrixResponse);

        // Assert that the extracted matrix matches the expected matrix
        assertEquals(expectedMatrix.size(), resultMatrix.size());
        for (int i = 0; i < expectedMatrix.size(); i++) {
            assertEquals(expectedMatrix.get(i), resultMatrix.get(i));
        }
    }

    @Test
     void testExtractDistanceMatrix_EmptyMatrix() throws JSONException {
        // Test with an empty distance matrix
        JSONObject distanceMatrixResponse = new JSONObject();
        JSONArray distances = new JSONArray();
        distanceMatrixResponse.put("distances", distances);

        // Expected output: an empty matrix
        List<List<Double>> expectedMatrix = new ArrayList<>();

        // Invoke the method
        List<List<Double>> resultMatrix = placeService.extractDistanceMatrix(distanceMatrixResponse);

        // Assert that the result is an empty list
        assertEquals(expectedMatrix, resultMatrix);
    }

    @Test
     void testExtractDurationMatrix() throws JSONException {
        // Create a mock JSON response representing a duration matrix
        JSONObject distanceMatrixResponse = new JSONObject();
        JSONArray durations = new JSONArray();

        // Constructing a 3x3 duration matrix as a sample
        durations.put(new JSONArray(Arrays.asList(0.0, 120.5, 250.3)));
        durations.put(new JSONArray(Arrays.asList(120.5, 0.0, 400.8)));
        durations.put(new JSONArray(Arrays.asList(250.3, 400.8, 0.0)));

        distanceMatrixResponse.put("durations", durations);

        // Expected output matrix
        List<List<Double>> expectedMatrix = Arrays.asList(
                Arrays.asList(0.0, 120.5, 250.3),
                Arrays.asList(120.5, 0.0, 400.8),
                Arrays.asList(250.3, 400.8, 0.0)
        );

        // Invoke the method
        List<List<Double>> resultMatrix = placeService.extractDurationMatrix(distanceMatrixResponse);

        // Assert that the extracted matrix matches the expected matrix
        assertEquals(expectedMatrix.size(), resultMatrix.size());
        for (int i = 0; i < expectedMatrix.size(); i++) {
            assertEquals(expectedMatrix.get(i), resultMatrix.get(i));
        }
    }


    @Test
     void testExtractDurationMatrix_EmptyMatrix() throws JSONException {
        // Test with an empty duration matrix
        JSONObject distanceMatrixResponse = new JSONObject();
        JSONArray durations = new JSONArray();
        distanceMatrixResponse.put("durations", durations);

        // Expected output: an empty matrix
        List<List<Double>> expectedMatrix = new ArrayList<>();

        // Invoke the method
        List<List<Double>> resultMatrix = placeService.extractDurationMatrix(distanceMatrixResponse);

        // Assert that the result is an empty list
        assertEquals(expectedMatrix, resultMatrix);
    }




//    @Test
//     void testSearchTestPlace_ValidInput() throws Exception {
//        // Setup valid parameters
//        Map<String, Object> params = new HashMap<>();
//        params.put("sourceCities", Arrays.asList("CityA"));
//        params.put("visitedCities", Arrays.asList("CityB","city"));
//        params.put("destinationCities", Arrays.asList("CityC"));
//        params.put("outboundDepartureDateStart", "2024-11-01");
//        params.put("inboundDepartureDateStart", "2024-11-10");
//        List<String> c=(List<String>) params.get("visitedCities");
//        // Mock repository returns
//        City cityB = new City();
//        CitySummary citySummaryB = new CitySummary();
//        citySummaryB.setId(1);
//        citySummaryB.setCity(cityB);
//        citySummaryB.setTotalDuration(5L);
//        cityB.setName("CityB");
//        cityB.setId(1);
//        when(cityRepository.findByName("CityB")).thenReturn(cityB);
//
//
//        // Set a valid total duration
//        when(citySummaryRepository.findByCity(cityB)).thenReturn(Optional.of(citySummaryB));
//
//        System.out.println("ff"+Optional.of(citySummaryB).get().getCity().getName());
//
//        // Assuming `place` has a proper constructor and required fields
//        Place place = new Place(); // Setup your Place object appropriately
//        when(placeRepository.search(eq(cityB.getId()), any(PageRequest.class)))
//                .thenReturn(new PageImpl<>(Collections.singletonList(place), PageRequest.of(0, 3), 1));
//
//        // Execute method
//        Object response = placeService.searchTestPlace(params);
//
//        // Verify response
//        assertNotNull(response);
//        // Add more assertions based on expected output
//    }






















}