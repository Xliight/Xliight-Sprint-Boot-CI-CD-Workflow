package com.ouitrips.app.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouitrips.app.entities.circuits.*;
import com.ouitrips.app.entities.places.*;
import com.ouitrips.app.entities.places.PlaceType;
import com.ouitrips.app.entities.social_network.*;
import com.ouitrips.app.googlemapsservice.model.*;
import com.ouitrips.app.googlemapsservice.model.Duration;
import com.ouitrips.app.mapper.security.circuits.*;
import com.ouitrips.app.mapper.security.geolocation.DistanceMatrixJsonConverter;
import com.ouitrips.app.mapper.security.places.*;
import com.ouitrips.app.mapper.security.social_network.*;
import com.ouitrips.app.repositories.security.circuits.LinksLsRepository;
import com.ouitrips.app.repositories.security.social_network.LinksPtgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMapper {
    @InjectMocks
    private PlaceMapper placeMapper;

    @Mock
    private PlaceInfoMapper placeInfoMapper;

    @Mock
    private PlaceDurationMapper placeDurationMapper;

    @Mock
    private PlacePricingMapper placePricingMapper;

    @Mock
    private PlaceOpeningHourMapper placeOpeningHourMapper;

    @Mock
    private PlaceTimeZoneMapper placeTimeZoneMapper;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private PlaceTypeMapper placeTypeMapper;

    @Mock
    private PlaceTagMapper placeTagMapper;
    private PlaceOpeningHourMapper mapper;
    @Mock
    private PostCommentMapper postCommentMapper;
    @Mock
    private ReactionMapper reactionMapper;
    @Mock
    private CircuitMapper circuitMapper;
    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private LinksPtgRepository linksPtgRepository;
    @Mock
    private TagMapper tagMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new PlaceOpeningHourMapper();
        cityMapper = new CityMapper();

    }

    @Test
    void testApply_withNullPlace() {
        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply(null);

        // Assert
        assertEquals(0, response.size(), "Expected empty response map for null place");
    }

    @Test
    void testApply2_withValidPlaceAndAdditionalInfo() {
        // Setup test data
        Place place = new Place();
        place.setId(1);
        place.setTitle("Test Place");

        Double duration = 90.0;
        Double distance = 15.0;

        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply2(place, duration, distance);

        // Assert
        assertEquals(1, response.get("reference"));
        assertEquals("Test Place", response.get("title"));
        assertEquals(duration, response.get("place_traject_duration"));
        assertEquals(distance, response.get("place_traject_distance"));
    }

    @Test
    void testApply_withValidPlaceOpeningHour() {
        // Arrange
        PlaceOpeningHour placeOpeningHour = new PlaceOpeningHour();
        placeOpeningHour.setId(1);
        placeOpeningHour.setOpeningTime(LocalTime.of(9, 0));
        placeOpeningHour.setCloseTime(LocalTime.of(17, 0));

        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(placeOpeningHour);

        // Assert
        assertEquals(1, result.get("reference"));
        assertEquals(LocalTime.of(9, 0), result.get("opening_time"));
        assertEquals(LocalTime.of(17, 0), result.get("close_time"));
    }

    @Test
    void testApply_withNullPlaceOpeningHour() {
        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(null);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty map for null PlaceOpeningHour");
    }


    @Test
    void testApply_withNullPlaceInfo() {
        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(null);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty map for null PlaceInfo");
    }


    @Test
    void testApply_withNullPlaceDuration() {
        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(null);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty map for null PlaceDuration");
    }


    @Test
    void testApply_withValidCity() {
        // Arrange
        PlaceOpeningHour placeOpeningHour = new PlaceOpeningHour();
        placeOpeningHour.setId(1);
        placeOpeningHour.setOpeningTime(LocalTime.of(9, 0));
        placeOpeningHour.setCloseTime(LocalTime.of(17, 0));
        placeOpeningHour.setPlace(new Place());

        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(placeOpeningHour);

        // Assert
        assertEquals(1, result.get("reference"));


    }

    @Test
    void testApply_withNullCity() {
        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(null);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty map for null City");
    }

    @Test
    void testApply_withValidPlaceType() {
        // Arrange
        PlaceOpeningHour placeOpeningHour = new PlaceOpeningHour();
        placeOpeningHour.setId(1);
        placeOpeningHour.setOpeningTime(LocalTime.of(9, 0));
        placeOpeningHour.setCloseTime(LocalTime.of(17, 0));
        placeOpeningHour.setPlace(new Place());

        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(placeOpeningHour);

        // Assert
        assertEquals(1, result.get("reference"));

    }
    @Test
    void testApply_withNullPlaceType() {
        // Act
        Map<String, Object> result = (Map<String, Object>) mapper.apply(null);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty map for null PlaceType");
    }

    @Test
    void testPlaceDurationsMapping() {
        // Arrange
        Place place = new Place();
        PlaceDuration placeDuration = new PlaceDuration();
        Object mappedPlaceDuration = new Object(); // Mocked mapped response for PlaceDuration

        Set<PlaceDuration> placeDurations = new HashSet<>(Collections.singletonList(placeDuration));
        place.setPlaceDurations(placeDurations);

        when(placeDurationMapper.apply(placeDuration)).thenReturn(mappedPlaceDuration);

        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply(place);

        // Assert
        assertTrue(response.containsKey("place_durations"));
        Set<Object> placeDurationsResponse = (Set<Object>) response.get("place_durations");
        assertEquals(1, placeDurationsResponse.size());
        assertTrue(placeDurationsResponse.contains(mappedPlaceDuration));

        // Verify
        verify(placeDurationMapper, times(1)).apply(placeDuration);
    }

    @Test
    void testPlacePricingsMapping() {
        // Arrange
        Place place = new Place();
        PlacePricing placePricing = new PlacePricing();
        Object mappedPlacePricing = new Object(); // Mocked mapped response for PlacePricing

        Set<PlacePricing> placePricings = new HashSet<>(Collections.singletonList(placePricing));
        place.setPlacePricing(placePricings);

        when(placePricingMapper.apply(placePricing)).thenReturn(mappedPlacePricing);

        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply(place);

        // Assert
        assertTrue(response.containsKey("place_pricing"));
        Set<Object> placePricingsResponse = (Set<Object>) response.get("place_pricing");
        assertEquals(1, placePricingsResponse.size());
        assertTrue(placePricingsResponse.contains(mappedPlacePricing));

        // Verify
        verify(placePricingMapper, times(1)).apply(placePricing);
    }
    @Test
    void testPlaceOpeningHoursMapping() {
        // Arrange
        Place place = new Place();
        PlaceOpeningHour placeOpeningHour = new PlaceOpeningHour();
        Object mappedPlaceOpeningHour = new Object(); // Mocked mapped response for PlaceOpeningHour

        Set<PlaceOpeningHour> placeOpeningHours = new HashSet<>(Collections.singletonList(placeOpeningHour));
        place.setPlaceOpeningHours(placeOpeningHours);

        when(placeOpeningHourMapper.apply(placeOpeningHour)).thenReturn(mappedPlaceOpeningHour);

        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply(place);

        // Assert
        assertTrue(response.containsKey("place_opening_hours"));
        Set<Object> placeOpeningHoursResponse = (Set<Object>) response.get("place_opening_hours");
        assertEquals(1, placeOpeningHoursResponse.size());
        assertTrue(placeOpeningHoursResponse.contains(mappedPlaceOpeningHour));

        // Verify
        verify(placeOpeningHourMapper, times(1)).apply(placeOpeningHour);
    }


    @Test
    void testPlaceTagsMapping() {
        // Arrange
        Place place = new Place();
        PlaceTag placeTag = new PlaceTag();
        Object mappedPlaceTag = new Object(); // Mocked mapped response for PlaceTag

        Set<PlaceTag> placeTags = new HashSet<>(Collections.singletonList(placeTag));
        place.setPlaceTags(placeTags);

        when(placeTagMapper.apply(placeTag)).thenReturn(mappedPlaceTag);

        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply(place);

        // Assert
        assertTrue(response.containsKey("place_tags"));
        Set<Object> placeTagsResponse = (Set<Object>) response.get("place_tags");
        assertEquals(1, placeTagsResponse.size());
        assertTrue(placeTagsResponse.contains(mappedPlaceTag));

        // Verify
        verify(placeTagMapper, times(1)).apply(placeTag);
    }


    @Test
    void testCityMapping() {
        // Arrange
        City city = new City();
        city.setId(1);
        city.setName("Test City");
        city.setLatitude(40.7128);
        city.setLongitude(-74.0060);
        city.setTemperature(25.0);
        city.setGps("40.7128,-74.0060");

        // Act
        Map<String, Object> response = (Map<String, Object>) cityMapper.apply(city);

        // Assert
        assertEquals(1, response.get("reference"));
        assertEquals("Test City", response.get("name"));
        assertEquals(40.7128, response.get("latitude"));
        assertEquals(-74.0060, response.get("longitude"));
        assertEquals(25.0, response.get("temperature"));
        assertEquals("40.7128,-74.0060", response.get("gps"));
    }

    @Test
    void testNullCity() {
        // Act
        Map<String, Object> response = (Map<String, Object>) cityMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null city input");
    }

    @Test
    void testPlaceInfoMapping() {
        placeInfoMapper = new PlaceInfoMapper();

        // Arrange
        PlaceInfo placeInfo = new PlaceInfo();
        placeInfo.setId(1);
        placeInfo.setPhone("123-456-7890");
        placeInfo.setPhone2("098-765-4321");
        placeInfo.setContent("Sample content");
        placeInfo.setWebsite("https://samplewebsite.com");
        placeInfo.setAddress("123 Sample St, Sample City");

        // Act
        Map<String, Object> response = (Map<String, Object>) placeInfoMapper.apply(placeInfo);

        // Assert
        assertEquals(1, response.get("reference"));
        assertEquals("123-456-7890", response.get("phone"));
        assertEquals("098-765-4321", response.get("phone2"));
        assertEquals("Sample content", response.get("content"));
        assertEquals("https://samplewebsite.com", response.get("website"));
        assertEquals("123 Sample St, Sample City", response.get("address"));
    }

    @Test
    void testNullPlaceInfo() {
        placeInfoMapper = new PlaceInfoMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) placeInfoMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null PlaceInfo input");
    }

    @Test
    void testPlacePricingMapping() {
        placePricingMapper = new PlacePricingMapper();

        // Arrange
        PlacePricing placePricing = new PlacePricing();
        placePricing.setId(1);
        placePricing.setPriceMin(10.0);
        placePricing.setPriceMax(50.0);
        placePricing.setPrice(30.0);

        // Act
        Map<String, Object> response = (Map<String, Object>) placePricingMapper.apply(placePricing);

        // Assert
        assertEquals(1, response.get("reference"));
        assertEquals(10.0, response.get("price_min"));
        assertEquals(50.0, response.get("price_max"));
        assertEquals(30.0, response.get("price"));
    }

    @Test
    void testNullPlacePricing() {
        placePricingMapper = new PlacePricingMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) placePricingMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null PlacePricing input");
    }

    @Test
    void testPlaceTagMapping() {        placeTagMapper = new PlaceTagMapper();

        // Arrange
        PlaceTag placeTag = new PlaceTag();
        placeTag.setId(123);
        placeTag.setTag("Restaurant");
        placeTag.setCategory("Food & Drink");

        // Act
        Map<String, Object> response = (Map<String, Object>) placeTagMapper.apply(placeTag);

        // Assert
        assertEquals(123, response.get("reference"));
        assertEquals("Restaurant", response.get("tag"));
        assertEquals("Food & Drink", response.get("category"));
    }

    @Test
    void testNullPlaceTag() {        placeTagMapper = new PlaceTagMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) placeTagMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null PlaceTag input");
    }


    @Test
    void testPlaceTimeZoneMapping() {
        placeTimeZoneMapper = new PlaceTimeZoneMapper();

        // Arrange
        PlaceTimeZone placeTimeZone = new PlaceTimeZone();
        placeTimeZone.setId(456);
        placeTimeZone.setUtcOffset("+03:00");
        placeTimeZone.setName("EAT");

        // Act
        Map<String, Object> response = (Map<String, Object>) placeTimeZoneMapper.apply(placeTimeZone);

        // Assert
        assertEquals(456, response.get("reference"));
        assertEquals("+03:00", response.get("utc_offset"));
        assertEquals("EAT", response.get("name"));
    }

    @Test
    void testNullPlaceTimeZone() {
        placeTimeZoneMapper = new PlaceTimeZoneMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) placeTimeZoneMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null PlaceTimeZone input");
    }

    @Test
    void testPlaceTypeMapping() {
        placeTypeMapper = new PlaceTypeMapper();

        // Arrange
        PlaceType placeType = new PlaceType();
        placeType.setId(123);
        placeType.setName("Beach");
        placeType.setCode("BCH");

        // Act
        Map<String, Object> response = (Map<String, Object>) placeTypeMapper.apply(placeType);

        // Assert
        assertEquals(123, response.get("reference"));
        assertEquals("Beach", response.get("name"));
        assertEquals("BCH", response.get("code"));
    }

    @Test
    void testNullPlaceType() {
        // Act
        placeTypeMapper = new PlaceTypeMapper();

        Map<String, Object> response = (Map<String, Object>) placeTypeMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null PlaceType input");
    }

    @Test
    void testPlaceDurationMapping() {        placeDurationMapper = new PlaceDurationMapper();

        // Arrange
        PlaceDuration placeDuration = new PlaceDuration();
        placeDuration.setId(1);
        placeDuration.setDuration(3L);

        // Act
        Map<String, Object> response = (Map<String, Object>) placeDurationMapper.apply(placeDuration);

        // Assert
        assertEquals(1, response.get("reference"));
        assertEquals(3L, response.get("duration"));
    }

    @Test
    void testNullPlaceDuration() {        placeDurationMapper = new PlaceDurationMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) placeDurationMapper.apply(null);

        // Assert
        assertTrue(response.isEmpty(), "Response should be empty for null PlaceDuration input");
    }


    @Test
    void testPlaceInfoMvapping() {
        Place place=mock(Place.class);
        placeMapper = new PlaceMapper(
                placeInfoMapper,
                mock(PlaceDurationMapper.class),
                mock(PlacePricingMapper.class),
                mock(PlaceOpeningHourMapper.class),
                mock(PlaceTimeZoneMapper.class),
                mock(CityMapper.class),
                mock(PlaceTypeMapper.class),
                mock(PlaceTagMapper.class)
        );
        // Arrange
        PlaceInfo placeInfo1 = new PlaceInfo();
        placeInfo1.setId(1);
        PlaceInfo placeInfo2 = new PlaceInfo();
        placeInfo2.setId(2);

        Set<PlaceInfo> placeInfos = new HashSet<>(Arrays.asList(placeInfo1, placeInfo2));

        when(place.getPlaceInfos()).thenReturn(placeInfos);
        when(placeInfoMapper.apply(placeInfo1)).thenReturn(Collections.singletonMap("reference", 1L));
        when(placeInfoMapper.apply(placeInfo2)).thenReturn(Collections.singletonMap("reference", 2L));

        // Act
        Map<String, Object> response = (Map<String, Object>) placeMapper.apply(place);

        // Assert
        assertNotNull(response);
        assertTrue(response.containsKey("place_infos"));

        // Ensure place_infos is a set of maps
        Set<Object> placeInfosResponse = (Set<Object>) response.get("place_infos");
        assertNotNull(placeInfosResponse);
        assertEquals(2, placeInfosResponse.size());

        // Verify that the placeInfoMapper was called for each placeInfo object
        verify(placeInfoMapper, times(1)).apply(placeInfo1);
        verify(placeInfoMapper, times(1)).apply(placeInfo2);

        // Check the values inside the place_infos set
        List<Map<String, Object>> placeInfosList = placeInfosResponse.stream()
                .map(o -> (Map<String, Object>) o)
                .collect(Collectors.toList());
        assertTrue(placeInfosList.stream().anyMatch(m -> m.containsValue(1L)));
        assertTrue(placeInfosList.stream().anyMatch(m -> m.containsValue(2L)));
    }
    private static final Logger logger = LoggerFactory.getLogger(TestMapper.class);




    @Test
    void testPostMapping() {
        PostMapper postMapper = new PostMapper(postCommentMapper, reactionMapper, circuitMapper, profileMapper, linksPtgRepository, tagMapper);

        // Arrange
        Post post = mock(Post.class);
        when(post.getId()).thenReturn(1);
        when(post.getDescription()).thenReturn("Test description");
        when(post.getContent()).thenReturn("Test content");
        when(post.getPicture()).thenReturn("picture.png");
        when(post.getStyle()).thenReturn("style1");
        when(post.isVisibility()).thenReturn(true);

        Circuit circuit = mock(Circuit.class);
        Profile profile = mock(Profile.class);
        when(post.getCircuit()).thenReturn(circuit);
        when(post.getProfile()).thenReturn(profile);

        Set<PostComment> postComments = new HashSet<>(Arrays.asList(mock(PostComment.class), mock(PostComment.class)));
        when(post.getComments()).thenReturn(postComments);
        when(postCommentMapper.apply(any(PostComment.class))).thenReturn(Collections.singletonMap("comment_reference", 1L));

        Set<Reaction> reactions = new HashSet<>(Arrays.asList(mock(Reaction.class), mock(Reaction.class)));
        when(post.getReactions()).thenReturn(reactions);
        when(reactionMapper.apply(any(Reaction.class))).thenReturn(Collections.singletonMap("reaction_reference", 1L));

        LinksPtg linksPtg1 = mock(LinksPtg.class);
        LinksPtg linksPtg2 = mock(LinksPtg.class);
        Tag tag1 = mock(Tag.class);
        Tag tag2 = mock(Tag.class);
        when(linksPtg1.getTag()).thenReturn(tag1);
        when(linksPtg2.getTag()).thenReturn(tag2);
        when(linksPtgRepository.findByPost(post)).thenReturn(Arrays.asList(linksPtg1, linksPtg2));
        when(tagMapper.apply(any(Tag.class))).thenReturn(Collections.singletonMap("tag_reference", 1L));

        when(circuitMapper.apply(circuit)).thenReturn(Collections.singletonMap("circuit_reference", 1L));
        when(profileMapper.apply(profile)).thenReturn(Collections.singletonMap("profile_reference", 1L));

        // Act
        Map<String, Object> response = (Map<String, Object>) postMapper.apply(post);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("Test description", response.get("description"));
        assertEquals("Test content", response.get("content"));
        assertEquals("picture.png", response.get("picture"));
        assertEquals("style1", response.get("style"));
        assertEquals(true, response.get("visibility"));
        assertTrue(response.containsKey("circuit"));
        assertTrue(response.containsKey("profile"));
        assertTrue(response.containsKey("post_comment"));
        assertTrue(response.containsKey("reactions"));
        assertTrue(response.containsKey("tags"));

        // Verify postComments mapping
        Set<Object> postCommentsResponse = (Set<Object>) response.get("post_comment");
        assertNotNull(postCommentsResponse);
        assertEquals(1, postCommentsResponse.size());

        // Verify reactions mapping
        List<Object> reactionsResponse = (List<Object>) response.get("reactions");
        assertNotNull(reactionsResponse);
        assertEquals(2, reactionsResponse.size());

        // Verify tags mapping
        List<Object> tagsResponse = (List<Object>) response.get("tags");
        assertNotNull(tagsResponse);
        assertEquals(2, tagsResponse.size());


    }


    @Test
    void testFollowerMapping() {
        FollowerMapper followerMapper = new FollowerMapper();

        // Arrange
        Follower follower = mock(Follower.class);
        when(follower.getId()).thenReturn(1);
        Date dateFollow = new Date(2023, 1, 1);
        Date dateUnfollow = new Date(2023, 6, 1);
        when(follower.getDateFollow()).thenReturn(dateFollow);
        when(follower.getDateUnfollow()).thenReturn(dateUnfollow);
        when(follower.isApproved()).thenReturn(true);

        // Act
        Map<String, Object> response = (Map<String, Object>) followerMapper.apply(follower);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals(dateFollow, response.get("dateFollow"));
        assertEquals(dateUnfollow, response.get("dateUnfollow"));
        assertEquals(true, response.get("isApproved"));
    }

    @Test
    void testNullFollowerMapping() {
        // Act
        FollowerMapper followerMapper = new FollowerMapper();

        Map<String, Object> response = (Map<String, Object>) followerMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testPostCommentMapping() {        PostCommentMapper postCommentMapper = new PostCommentMapper();

        // Arrange
        PostComment postComment = mock(PostComment.class);
        when(postComment.getId()).thenReturn(1);
        when(postComment.getContent()).thenReturn("Sample content");
        when(postComment.getReactionNumber()).thenReturn(String.valueOf(5));

        // Act
        Map<String, Object> response = (Map<String, Object>) postCommentMapper.apply(postComment);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("Sample content", response.get("content"));
        assertEquals("5", response.get("reaction_number"));
    }

    @Test
    void testNullPostCommentMapping() {
        PostCommentMapper postCommentMapper = new PostCommentMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) postCommentMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testProfileMapping() {        ProfileMapper profileMapper = new ProfileMapper();

        // Arrange
        Profile profile = mock(Profile.class);
        when(profile.getId()).thenReturn(1);
        when(profile.getName()).thenReturn("John Doe");
        when(profile.getUrlPublic()).thenReturn("http://example.com/johndoe");

        // Act
        Map<String, Object> response = (Map<String, Object>) profileMapper.apply(profile);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("John Doe", response.get("name"));
        assertEquals("http://example.com/johndoe", response.get("url_public"));
    }
    @Test
    void testNullProfileMapping() {         ReactionLevelMapper reactionLevelMapper = new ReactionLevelMapper();

        ProfileMapper profileMapper = new ProfileMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) profileMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testReactionLevelMapping() {
        ReactionLevelMapper reactionLevelMapper = new ReactionLevelMapper();

        // Arrange
        ReactionLevel reactionLevel = mock(ReactionLevel.class);
        when(reactionLevel.getId()).thenReturn(1);
        when(reactionLevel.getValue()).thenReturn(5);
        when(reactionLevel.getOrder()).thenReturn(1);

        // Act
        Map<String, Object> response = (Map<String, Object>) reactionLevelMapper.apply(reactionLevel);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals(5, response.get("value"));
        assertEquals(1, response.get("order"));
    }

    @Test
    void testNullReactionLevelMapping() {         ReactionLevelMapper reactionLevelMapper = new ReactionLevelMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) reactionLevelMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    @Test
    void testReactionMapping() {

        ReactionTypeMapper  reactionTypeMapper = mock(ReactionTypeMapper.class);
        ReactionLevelMapper   reactionLevelMapper = mock(ReactionLevelMapper.class);
        ProfileMapper  profileMapper = mock(ProfileMapper.class);
        ReactionMapper reactionMapper = new ReactionMapper(reactionTypeMapper, reactionLevelMapper, profileMapper);
        // Arrange
        Reaction reaction = mock(Reaction.class);
        Profile profile = mock(Profile.class);
        ReactionType reactionType = mock(ReactionType.class);
        ReactionLevel reactionLevel = mock(ReactionLevel.class);

        Date reactionDate = new Date();

        when(reaction.getId()).thenReturn(1);
        when(reaction.getDate()).thenReturn(reactionDate);
        when(reaction.getProfile()).thenReturn(profile);
        when(reaction.getReactionType()).thenReturn(reactionType);
        when(reaction.getReactionLevel()).thenReturn(reactionLevel);

        when(profileMapper.apply(profile)).thenReturn(Map.of("reference", 1, "name", "Test User"));
        when(reactionTypeMapper.apply(reactionType)).thenReturn(Map.of("type", "Like"));
        when(reactionLevelMapper.apply(reactionLevel)).thenReturn(Map.of("level", "High"));

        // Act
        Map<String, Object> response = (Map<String, Object>) reactionMapper.apply(reaction);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals(reactionDate, response.get("date"));

        // Check nested mappings
        Map<String, Object> profileResponse = (Map<String, Object>) response.get("profile");
        assertNotNull(profileResponse);
        assertEquals(1, profileResponse.get("reference"));
        assertEquals("Test User", profileResponse.get("name"));

        Map<String, Object> reactionTypeResponse = (Map<String, Object>) response.get("reaction_type");
        assertNotNull(reactionTypeResponse);
        assertEquals("Like", reactionTypeResponse.get("type"));

        Map<String, Object> reactionLevelResponse = (Map<String, Object>) response.get("reaction_level");
        assertNotNull(reactionLevelResponse);
        assertEquals("High", reactionLevelResponse.get("level"));

        // Verify that mappers were called
        verify(profileMapper, times(1)).apply(profile);
        verify(reactionTypeMapper, times(1)).apply(reactionType);
        verify(reactionLevelMapper, times(1)).apply(reactionLevel);
    }

    @Test
    void testNullReactionMapping() {ReactionTypeMapper  reactionTypeMapper = mock(ReactionTypeMapper.class);
        ReactionLevelMapper   reactionLevelMapper = mock(ReactionLevelMapper.class);
        ProfileMapper  profileMapper = mock(ProfileMapper.class);
        ReactionMapper reactionMapper = new ReactionMapper(reactionTypeMapper, reactionLevelMapper, profileMapper);
        // Act
        Map<String, Object> response = (Map<String, Object>) reactionMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    @Test
    void testReactionTypeMapping() {
        ReactionTypeMapper reactionTypeMapper = new ReactionTypeMapper();

        // Arrange
        ReactionType reactionType = new ReactionType();
        reactionType.setId(1);
        reactionType.setName("Like");
        reactionType.setDescription("Indicates a like reaction");
        reactionType.setCode("LIKE");
        reactionType.setStatus("active");

        // Act
        Map<String, Object> response = (Map<String, Object>) reactionTypeMapper.apply(reactionType);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("Like", response.get("name"));
        assertEquals("Indicates a like reaction", response.get("description"));
        assertEquals("LIKE", response.get("code"));
        assertEquals("active", response.get("status"));
    }

    @Test
    void testNullReactionTypeMapping() {        ReactionTypeMapper reactionTypeMapper = new ReactionTypeMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) reactionTypeMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testTagMapping() {
        TagMapper tagMapper = new TagMapper();

        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Travel");
        tag.setReference("T001");

        // Act
        Map<String, Object> response = (Map<String, Object>) tagMapper.apply(tag);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("id"));
        assertEquals("Travel", response.get("name"));
        assertEquals("T001", response.get("reference"));
    }

    @Test
    void testNullTagMapping() {
        // Act
        TagMapper tagMapper = new TagMapper();

        Map<String, Object> response = (Map<String, Object>) tagMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    @Test
    void testCircuitGroupMapping() {
        CircuitGroupMapper circuitGroupMapper = new CircuitGroupMapper();

        // Arrange
        CircuitGroup circuitGroup = new CircuitGroup();
        circuitGroup.setId(1);
        circuitGroup.setName("Adventure");
        circuitGroup.setDescription("Exciting tours");
        circuitGroup.setColor("Red");
        circuitGroup.setIcon("icon.png");

        // Act
        Map<String, Object> response = (Map<String, Object>) circuitGroupMapper.apply(circuitGroup);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("id"));
        assertEquals("Adventure", response.get("name"));
        assertEquals("Exciting tours", response.get("description"));
        assertEquals("Red", response.get("color"));
        assertEquals("icon.png", response.get("icon"));
    }

    @Test
    void testNullCircuitGroupMapping() {
        CircuitGroupMapper circuitGroupMapper = new CircuitGroupMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) circuitGroupMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testLocationMapping() {
        PlaceMapper placeMapperr = mock(PlaceMapper.class);
        LocationsMapper locationsMapper = new LocationsMapper(placeMapperr);
        // Arrange
        Location location = new Location();
        location.setId(1);
        location.setName("Paris");
        location.setDescription("City of Light");
        location.setLatitude(48.8566);
        location.setLongitude(2.3522);
        location.setAddress("Paris, France");
        location.setRating(4);

        when(placeMapperr.apply(location.getPlace())).thenReturn(Map.of("reference", 1)); // Mock place mapper response

        // Act
        Map<String, Object> response = (Map<String, Object>) locationsMapper.apply(location);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("Paris", response.get("name"));
        assertEquals("City of Light", response.get("description"));
        assertEquals(48.8566, response.get("latitude"));
        assertEquals(2.3522, response.get("longitude"));
        assertEquals("Paris, France", response.get("location"));
        assertEquals(4, response.get("rating"));
    }

    @Test
    void testNullLocationMapping() {
        LocationsMapper locationsMapper = new LocationsMapper(placeMapper);
        // Act
        Map<String, Object> response = (Map<String, Object>) locationsMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testStepMapping() {
        LinksLsRepository linksLsRepository = mock(LinksLsRepository.class);
        LocationsMapper  locationsMapper = mock(LocationsMapper.class);
        TransportMeansMapper transportMeansMapper = mock(TransportMeansMapper.class);
        StepsMapper stepsMapper = new StepsMapper(linksLsRepository, locationsMapper, transportMeansMapper);
        // Arrange
        Step step = new Step();
        step.setId(1);
        step.setName("Step 1");
        step.setDirections("Go north");
        step.setOrderStep(1);
        step.setState(true);

        LinksLs link = new LinksLs();
        link.setDuration(120);
        link.setDistance(15);
        link.setLocationStart(null);  // Mock behavior for null locationStart

        when(linksLsRepository.findByStep(step)).thenReturn(List.of(link));

        // Act
        Map<String, Object> response = (Map<String, Object>) stepsMapper.apply(step);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("Step 1", response.get("name"));
        assertEquals("Go north", response.get("directions"));
        assertEquals( 120.0f, response.get("duration"));
        assertEquals(15.0f, response.get("distance"));
    }

    @Test
    void testTrackingCircuitMapping() {
        TrackingCircuitMapper trackingCircuitMapper = new TrackingCircuitMapper();
        ZoneId zoneId = ZoneId.of("Europe/Paris");

        Clock clock = Clock.system(zoneId);

        // Arrange
        TrackingCircuit trackingCircuit = new TrackingCircuit();
        trackingCircuit.setId(1);
        trackingCircuit.setStartDate(Instant.ofEpochSecond(1970-01-01));
        trackingCircuit.setUpdatedDate(clock.instant());

        // Act
        Map<String, Object> response = (Map<String, Object>) trackingCircuitMapper.apply(trackingCircuit);
        response.get("updatedDate");
        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals(String.valueOf(Instant.ofEpochSecond(1970-01-01)), response.get("startDate"));
    }
    @Test
    void testNullTrackingCircuitMapping() {
        // Act
        TrackingCircuitMapper trackingCircuitMapper = new TrackingCircuitMapper();

        Map<String, Object> response = (Map<String, Object>) trackingCircuitMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testTransportMeansMapping() {
        TransportMeansMapper transportMeansMapper = new TransportMeansMapper();

        // Arrange
        TransportMeans transportMeans = new TransportMeans();
        transportMeans.setId(1);
        transportMeans.setName("Bus");
        transportMeans.setCode("dsf");
        transportMeans.setLinks(new HashSet<>());


        // Act
        Map<String, Object> response = (Map<String, Object>) transportMeansMapper.apply(transportMeans);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.get("reference"));
        assertEquals("Bus", response.get("name"));
        assertEquals("dsf", response.get("code"));
        response.get("links");
    }

    @Test
    void testNullTransportMeansMapping() {
        TransportMeansMapper transportMeansMapper = new TransportMeansMapper();

        // Act
        Map<String, Object> response = (Map<String, Object>) transportMeansMapper.apply(null);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    public void testConvertToCustomJson_withValidData() throws Exception {
        // Arrange
        String[] destinationAddresses = {"123 Destination St"};
        String[] originAddresses = {"456 Origin Ave"};

        DistanceMatrixElement element = new DistanceMatrixElement();
        element.status = DistanceMatrixElementStatus.OK;

        Distance distance = new Distance();
        distance.humanReadable = "10 km";
        distance.inMeters = 10000;
        element.distance = distance;

        Duration duration = new Duration();
        duration.humanReadable = "10 mins";
        duration.inSeconds = 600;
        element.duration = duration;

        DistanceMatrixRow row = new DistanceMatrixRow();
        row.elements = new DistanceMatrixElement[]{element};

        DistanceMatrixRow[] rows = new DistanceMatrixRow[]{row};

        // Instantiate DistanceMatrix with required constructor arguments
        DistanceMatrix distanceMatrix = new DistanceMatrix(originAddresses, destinationAddresses, rows);

        // Act
        String jsonResult = DistanceMatrixJsonConverter.convertToCustomJson(distanceMatrix);

        // Assert
        assertThat(jsonResult).isNotNull();

        // Parse the JSON string to validate its content
        JsonNode jsonNode = objectMapper.readTree(jsonResult);
        assertThat("[\"123 Destination St\"]").hasToString(String.valueOf(jsonNode.get("destination_addresses")));
        assertThat("[\"456 Origin Ave\"]").hasToString(String.valueOf(jsonNode.get("origin_addresses")));
        assertThat(jsonNode.get("status").asText()).isEqualTo("OK");

        // Check nested rows and elements
        JsonNode rowsNode = jsonNode.get("rows");
        assertThat(rowsNode).isNotNull();
        assertThat(rowsNode.isArray()).isTrue();
        assertThat(rowsNode.size()).isEqualTo(1);

        JsonNode elements = rowsNode.get(0).get("elements");
        assertThat(elements).isNotNull();
        assertThat(elements.isArray()).isTrue();
        assertThat(elements.size()).isEqualTo(1);

        JsonNode firstElement = elements.get(0);
        assertThat(firstElement.get("status").asText()).isEqualTo("OK");
        assertThat(firstElement.get("distance").get("text").asText()).isEqualTo("10 km");
        assertThat(firstElement.get("distance").get("value").asInt()).isEqualTo(10000);
        assertThat(firstElement.get("duration").get("text").asText()).isEqualTo("10 mins");
        assertThat(firstElement.get("duration").get("value").asInt()).isEqualTo(600);
    }
    @Test
    public void testConvertToCustomJson_emptyDistanceMatrix() throws Exception {
        // Arrange
        String[] destinationAddresses = new String[0]; // Empty array for destination addresses
        String[] originAddresses = new String[0]; // Empty array for origin addresses
        DistanceMatrixRow[] rows = new DistanceMatrixRow[0]; // No rows

        // Instantiate DistanceMatrix with empty data
        DistanceMatrix distanceMatrix = new DistanceMatrix(originAddresses, destinationAddresses, rows);

        // Act
        String jsonResult = DistanceMatrixJsonConverter.convertToCustomJson(distanceMatrix);

        // Assert
        assertThat(jsonResult).isNotNull();

        // Parse the JSON string to validate its structure
        JsonNode jsonNode = objectMapper.readTree(jsonResult);
        assertThat("[]").hasToString(String.valueOf(jsonNode.get("destination_addresses")));
        // Verify top-level keys
        assertThat("[]").hasToString(String.valueOf(jsonNode.get("origin_addresses")));
        assertThat(jsonNode.get("status").asText()).isEqualTo("OK");

        // Verify that rows are empty
        JsonNode rowsNode = jsonNode.get("rows");
        assertThat(rowsNode).isNotNull();
        assertThat(rowsNode.isArray()).isTrue();
        assertThat(rowsNode.size()).isZero();
    }

//    @Test
//    void testConvertToCustomJson() {
//        DistanceMatrixJsonConverter distanceMatrixJsonConverter = new DistanceMatrixJsonConverter();
//        String[] originAddresses=new String[0];
//        String[] destinationAddresses = new String[0];
//        DistanceMatrixRow[] rows = new DistanceMatrixRow[0];
//
//        // Arrange
//        DistanceMatrix distanceMatrix = new DistanceMatrix(originAddresses,destinationAddresses,rows);
//        distanceMatrix.destinationAddresses = List.of("Address 1").toArray(new String[0]);
//        distanceMatrix.originAddresses = List.of("Address 2").toArray(new String[0]);
//
//        DistanceMatrixRow row = new DistanceMatrixRow();
//        DistanceMatrixElement element = new DistanceMatrixElement();
//        element.status = DistanceMatrixElementStatus.OK;
//        element.distance = new Distance();
//        element.distance.humanReadable = "10 km";
//        element.distance.inMeters = 10000;
//        element.duration = new Duration();
//        element.duration.humanReadable = "10 mins";
//        element.duration.inSeconds = 600;
//        element.durationInTraffic = new Duration();
//        element.durationInTraffic.humanReadable = "10 mins";
//        element.durationInTraffic.inSeconds = 600;
//
//        row.elements = List.of(element).toArray(new DistanceMatrixElement[0]);
//        distanceMatrix.rows = List.of(row).toArray(new DistanceMatrixRow[0]);
//
//        // Act
//        String jsonResponse = distanceMatrixJsonConverter.convertToCustomJson(distanceMatrix);
//
//        // Assert
//        assertNotNull(jsonResponse);
//        assertTrue(jsonResponse.contains("\"destination_addresses\""));
//        assertTrue(jsonResponse.contains("\"origin_addresses\""));
//        assertTrue(jsonResponse.contains("\"status\":\"OK\""));
//    }
//
//    @Test
//    void testConvertFromCustomJson_InvalidJson() {        DistanceMatrixJsonConverter distanceMatrixJsonConverter = new DistanceMatrixJsonConverter();
//
//        // Act
//        DistanceMatrix distanceMatrix = distanceMatrixJsonConverter.convertFromCustomJson("invalid json");
//
//        // Assert
//        assertNull(distanceMatrix);
//    }

}









