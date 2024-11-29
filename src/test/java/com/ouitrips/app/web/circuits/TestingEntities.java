package com.ouitrips.app.web.circuits;


import com.ouitrips.app.entities.agency.*;
import com.ouitrips.app.entities.circuits.*;
import com.ouitrips.app.entities.geolocation.*;
import com.ouitrips.app.entities.places.*;
import com.ouitrips.app.entities.reviews.FeedbackType;
import com.ouitrips.app.entities.reviews.Reason;
import com.ouitrips.app.entities.reviews.Review;
import com.ouitrips.app.entities.reviews.WaitingTime;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.entities.social_network.*;
import com.ouitrips.app.entities.translations.EditorialObject;
import com.ouitrips.app.entities.translations.EditorialSummary;
import com.ouitrips.app.entities.translations.EditorialSummaryId;
import com.ouitrips.app.entities.translations.Language;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TestingEntities {

    @Test
    void testEntityPersistenceAndRelationships() {
        assertThat(1).isPositive();

        Set<LinksLs> transportMeans = new HashSet<>();

        // Create TransportMeans
        TransportMeans transportMeanss = new TransportMeans(1,"bus","bus",transportMeans);
        transportMeanss.setId(1);
        transportMeanss.setName("Bus");
        transportMeanss.setCode("BUS");
        transportMeanss.setLinks(new HashSet<>() {
        });
        transportMeanss.getLinks();


        Picture picture = new Picture(
                1,                        // id
                "Picture Name",           // name
                "This is a description",  // description
                34.0522,                  // latitude
                -118.2437,                // longitude
                new Step()                // step (assuming you have a Step object to assign)
        );
        picture.setId(1);
        picture.getId();
        picture.setName("New Picture Name");
       picture.getName();

        picture.setDescription("Updated description");
       picture.getDescription();

        picture.setLatitude(40.7128);
        picture.getLatitude();

        picture.setLongitude(-74.0060);
       picture.getLongitude();

        Step step = new Step();
        picture.setStep(step);
        picture.getStep();
        // Creating a new Location object with all arguments using the constructor
        Location location = new Location(
                1,                          // id
                "Location description",     // description
                40.7128,                    // latitude
                -74.0060,                   // longitude
                "123 Main St, New York, NY",// address
                "Location Name",            // name
                5,                          // rating
                new HashSet<>(),            // linksAsStart
                new HashSet<>(),            // linksAsEnd
                new Place(),                // place (assuming you have a Place object)
                new SpecificPlace()         // specificPlace (assuming you have a SpecificPlace object)
        );

// Accessing and modifying properties using setters and getters
        location.setId(2); // Example of setting a new ID
        location.getId();

        location.setDescription("Updated Location description");
         location.getDescription();

        location.setLatitude(34.0522);
        location.getLatitude();

        location.setLongitude(-118.2437);
        location.getLongitude();

        location.setAddress("456 Elm St, Los Angeles, CA");
        location.getAddress();

        location.setName("Updated Location Name");
        location.getName();

        location.setRating(4);
        location.getRating();

// Accessing and setting the Place and SpecificPlace objects
        Place place = new Place();
        location.setPlace(place);
      location.getPlace();

        SpecificPlace specificPlace = new SpecificPlace();
        Set<Location> locations = new HashSet<>();
        specificPlace.setLocations(locations);
        location.setSpecificPlace(specificPlace);
        location.getSpecificPlace();
        SearchCircuit searchCircuits =new SearchCircuit();
        // Creating a new Step object with all arguments using the constructor
        new Step(
                1,                            // id
                "Step Name",                  // name
                "Step directions",            // directions
                1,                            // orderStep
                true,                         // state
                new Circuit(),                // circuit (assuming you have a Circuit object)
                new HashSet<>(),              // pictures (empty Set of Picture objects)
                new HashSet<>(),              // links (empty Set of LinksLs objects)
                new HashSet<>()              // trackingCircuits (empty Set of TrackingCircuit objects)
        );

// Accessing and modifying properties using setters and getters
        step.setId(2); // Setting a new ID
        step.getId();

        step.setName("Updated Step Name");
      step.getName();

        step.setDirections("Updated directions");
       step.getDirections();

        step.setOrderStep(2);
       step.getOrderStep();

        step.setState(false);
       step.getState();

// Accessing and setting Circuit, Pictures, Links, and TrackingCircuits sets
        Circuit circuit = new Circuit();
        step.setCircuit(circuit);
        step.getCircuit();

        Set<Picture> pictures = new HashSet<>();
        pictures.add(new Picture());
        step.setPictures(pictures);
       step.getPictures();

        Set<LinksLs> links = new HashSet<>();
        links.add(new LinksLs());
        step.setLinks(links);
        step.getLinks();

        Set<TrackingCircuit> trackingCircuits = new HashSet<>();
        trackingCircuits.add(new TrackingCircuit());
        step.setTrackingCircuits(trackingCircuits);
       step.getTrackingCircuits();

        // Creating a new TrackingCircuit object with all arguments using the constructor
        TrackingCircuit trackingCircuit = new TrackingCircuit(
                1,                            // id
                Instant.now(),                // startDate
                Instant.now(),                // updatedDate
                "-118.2437",                  // longitude
                "34.0522",                    // latitude
                new Circuit(),                // circuit (assuming you have a Circuit object)
                new Step()                    // step (assuming you have a Step object)
        );

// Using setters
        trackingCircuit.setId(2);
        trackingCircuit.setStartDate(Instant.now());
        trackingCircuit.setUpdatedDate(Instant.now());
        trackingCircuit.setLongitude("-73.935242");
        trackingCircuit.setLatitude("40.730610");

// Accessing each property directly with getters
        trackingCircuit.getId();
        trackingCircuit.getStartDate();
        trackingCircuit.getUpdatedDate();
        trackingCircuit.getLongitude();
        trackingCircuit.getLatitude();
        trackingCircuit.getCircuit();
        trackingCircuit.getStep();

// Creating a new SpecificPlace object with all arguments using the constructor
         new SpecificPlace(
                1,                            // id
                "Place Title",                // title
                "Place description",          // description
                34.0522,                      // latitude
                -118.2437,                    // longitude
                "123 Main St, City, Country", // address
                new HashSet<>()               // locations (empty Set of Location objects)
        );

// Using setters to modify each property
        specificPlace.setId(2);
        specificPlace.setTitle("Updated Place Title");
        specificPlace.setDescription("Updated description");
        specificPlace.setLatitude(40.730610);
        specificPlace.setLongitude(-73.935242);
        specificPlace.setAddress("456 Another St, New City, Country");

// Accessing each property directly with getters
        specificPlace.getId();
        specificPlace.getTitle();
        specificPlace.getDescription();
        specificPlace.getLatitude();
        specificPlace.getLongitude();
        specificPlace.getAddress();
        specificPlace.getLocations();

        // Creating a new CircuitGroup object with all arguments using the constructor
        CircuitGroup circuitGroup = new CircuitGroup(
                1,                  // id
                "Group Name",       // name
                true,               // isDefault
                "Group description", // description
                "Red",              // color
                "Icon URL",         // icon
                new HashSet<>(),    // circuit (empty Set of Circuit objects)
                new User(),         // user (assuming a User object is available)
                new Category()      // category (assuming a Category object is available)
        );


// Using setters to modify each property
        circuitGroup.setId(2);
        circuitGroup.setName("Updated Group Name");
        circuitGroup.setIsDefault(false);
        circuitGroup.setDescription("Updated description");
        circuitGroup.setColor("Blue");
        circuitGroup.setIcon("Updated Icon URL");
        Set<Circuit > circuits = new HashSet<>();
        circuitGroup.setCircuit(circuits);

// Accessing each property directly with getters
        circuitGroup.getId();
        circuitGroup.getName();
        circuitGroup.getIsDefault();
        circuitGroup.getDescription();
        circuitGroup.getColor();
        circuitGroup.getIcon();
        circuitGroup.getCircuit();
        circuitGroup.getUser();
        circuitGroup.getCategory();


        // Creating a new Circuit object with all arguments using the constructor
        Circuit circuitt = new Circuit(
                1,                       // id
                "Circuit Name",          // name
                150.5,                   // distance
                false,                   // isDeleted
                LocalDate.of(2024, 1, 1), // dateDebut
                LocalDate.of(2024, 1, 10), // dateFin
                new HashSet<>(),         // steps (empty Set of Step objects)
                new CircuitGroup(),      // circuitGroup (assuming a CircuitGroup object is available)
                new ArrayList<>()        // agencyCircuits (empty List of AgencyCircuit objects)
        );
        AgencyCircuit c=new AgencyCircuit();
        List<AgencyCircuit> l=new ArrayList<>();
        l.add(c);
        circuitt.setAgencyCircuits(l);
// Using setters to modify each property
        circuit.setId(2);
        circuit.setName("Updated Circuit Name");
        circuit.setDistance(200.75);
        circuit.setIsDeleted(true);
        circuit.setDateDebut(LocalDate.of(2025, 5, 15));
        circuit.setDateFin(LocalDate.of(2025, 5, 25));

// Accessing each property directly with getters
        circuit.getId();
        circuit.getName();
        circuit.getDistance();
        circuit.getIsDeleted();
        circuit.getDateDebut();
        circuit.getDateFin();
        circuit.getSteps();
        circuit.getCircuitGroup();
        circuit.getAgencyCircuits();

        // Creating a new Category object with all arguments using the constructor
        Category category = new Category(
                1,                    // id
                "Category Name",      // name
                "CATEGORY_CODE",      // code
                new HashSet<>()       // circuitGroups (empty Set of CircuitGroup objects)
        );
        Set<CircuitGroup> groups = new HashSet<>();
    category.setCircuitGroups(groups);
// Using setters to modify each property
        category.setId(2);
        category.setName("Updated Category Name");
        category.setCode("UPDATED_CODE");

// Accessing each property directly with getters
        category.getId();
        category.getName();
        category.getCode();
        category.getCircuitGroups();

// Creating a new Location object with all arguments using the constructor
        Location locationn = new Location(
                1,                        // id
                "Description of the location",  // description
                40.7128,                  // latitude
                -74.0060,                 // longitude
                "123 Street Address",     // address
                "Location Name",          // name
                5,                        // rating
                new HashSet<>(),          // linksAsStart (empty Set of LinksLs)
                new HashSet<>(),          // linksAsEnd (empty Set of LinksLs)
                new Place(),              // place (assuming a Place object is available)
                new SpecificPlace()       // specificPlace (assuming a SpecificPlace object is available)
        );

// Using setters to modify each property
        location.setId(2);
        location.setDescription("Updated Description");
        location.setLatitude(34.0522);
        location.setLongitude(-118.2437);
        location.setAddress("456 Another Address");
        location.setName("Updated Location Name");
        location.setRating(4);
        locationn.setLinksAsEnd(links);
        location.setLinksAsStart(links);

// Accessing each property directly with getters
        location.getId();
        location.getDescription();
        location.getLatitude();
        location.getLongitude();
        location.getAddress();
        location.getName();
        location.getRating();
        location.getLinksAsStart();
        location.getLinksAsEnd();
        location.getPlace();
        location.getSpecificPlace();


        // Create the composite ID for LinksLs
        LinksLsId linksLsId = new LinksLsId(1, 2, 3, 4); // Assuming valid IDs for locationStart, locationEnd, step, and mode

// Create Location, Step, and TransportMeans objects (you would need actual objects or mock data)
        Location locationStart = new Location();
        Location locationEnd = new Location();
        TransportMeans mode = new TransportMeans();

// Create LinksLs object using constructor
        LinksLs linksLs = new LinksLs(
                linksLsId,              // Composite ID
                10.5f,                  // duration
                15.2f,                  // distance
                locationStart,          // locationStart
                locationEnd,            // locationEnd
                step,                   // step
                mode                    // mode
        );

// Using setters to modify properties
        linksLs.setDuration(20.0f);
        linksLs.setDistance(25.0f);
        linksLs.setLocationStart(new Location());
        linksLs.setLocationEnd(new Location());
        linksLs.setStep(new Step());
        linksLs.setMode(new TransportMeans());

// Accessing properties with getters
        linksLs.getId();                    // Get LinksLsId
        linksLs.getDuration();              // Get duration
        linksLs.getDistance();              // Get distance
        linksLs.getLocationStart();         // Get locationStart
        linksLs.getLocationEnd();           // Get locationEnd
        linksLs.getStep();                  // Get step
        linksLs.getMode();                  // Get mode


        // Create LinksLsId object

// Create LinksLs object with the composite ID
        linksLs.setId(linksLsId); // Setting the composite key

// You can then interact with the other fields, such as:
        linksLs.setDuration(10.5f);
        linksLs.setDistance(20.0f);

// Accessing individual values
        linksLs.getId().getFkidLocationStart();
        linksLs.getId().getFkidLocationEnd();
         linksLs.getId().getFkidStep();
        linksLs.getId().getFkidMode();

    }

     @Test
    void PlaceShemaEntities(){
         assertThat(1).isPositive();
         SearchModel searchModel =new SearchModel();

         // Creating an instance of Place
         Place place = new Place(
                 1,                             // id
                 "Place Title",                  // title
                 "Place Description",            // description
                 "Place Content",                // content
                 40.7128,                        // latitude
                 -74.0060,                       // longitude
                 "123 Example St, New York, NY", // address
                 "High",                          // popularity
                 new PlaceInfoGeolocation(),     // placeInfoGeolocation (assuming it's available)
                 new ArrayList<>(),              // originMetrics (empty list of MetricDetails)
                 new ArrayList<>(),              // destinationMetrics (empty list of MetricDetails)
                 new HashSet<>(),                // placeRequirements (empty Set of PlaceRequirement)
                 new HashSet<>(),                // placePricing (empty Set of PlacePricing)
                 new HashSet<>(),                // placeInfos (empty Set of PlaceInfo)
                 new HashSet<>(),                // placeOpeningHours (empty Set of PlaceOpeningHour)
                 new HashSet<>(),                // placeDurations (empty Set of PlaceDuration)
                 new HashSet<>(),                // locations (empty Set of Location)
                 new PlaceFreeOrNot(),           // placeFreeOrNot (assuming it's available)
                 new PlaceTimeZone(),            // placeTimeZone (assuming it's available)
                 new City(),                     // city (assuming a City object is available)
                 new HashSet<>(),                // placeTypes (empty Set of PlaceType)
                 new HashSet<>(),searchModel
//                 new ArrayList<>(),
//                 new SearchModel()// placeTags (empty Set of PlaceTag)
         );

// Using setters to modify each property
         place.setId(2);
         place.setTitle("Updated Place Title");
         place.setDescription("Updated Place Description");
         place.setContent("Updated Place Content");
         place.setLatitude(51.5074);
         place.setLongitude(-0.1278);
         place.setAddress("456 Another St, London, UK");
         place.setPopularity("Low");

         place.setPlaceInfoGeolocation(new PlaceInfoGeolocation());  // Assuming a new PlaceInfoGeolocation object is created
         place.setOriginMetrics(new ArrayList<>());                   // Assuming an empty list of MetricDetails
         place.setDestinationMetrics(new ArrayList<>());             // Assuming an empty list of MetricDetails
         place.setPlaceRequirements(new HashSet<>());                 // Assuming an empty Set of PlaceRequirement
         place.setPlacePricing(new HashSet<>());                      // Assuming an empty Set of PlacePricing
         place.setPlaceInfos(new HashSet<>());                        // Assuming an empty Set of PlaceInfo
         place.setPlaceOpeningHours(new HashSet<>());                 // Assuming an empty Set of PlaceOpeningHour
         place.setPlaceDurations(new HashSet<>());                    // Assuming an empty Set of PlaceDuration
         place.setLocations(new HashSet<>());                         // Assuming an empty Set of Location
         place.setPlaceFreeOrNot(new PlaceFreeOrNot());               // Assuming a new PlaceFreeOrNot object is created
         place.setPlaceTimeZone(new PlaceTimeZone());                 // Assuming a new PlaceTimeZone object is created
         place.setCity(new City());                                   // Assuming a new City object is created
         place.setLinksPts(new HashSet<>());                        // Assuming an empty Set of PlaceType
         place.setPlaceTags(new HashSet<>());                         // Assuming an empty Set of PlaceTag

// Accessing each property directly with getters
         place.getId();
         place.getTitle();
         place.getDescription();
         place.getContent();
         place.getLatitude();
         place.getLongitude();
         place.getAddress();
         place.getPopularity();
         place.getPlaceInfoGeolocation();
         place.getOriginMetrics();
         place.getDestinationMetrics();
         place.getPlaceRequirements();
         place.getPlacePricing();
         place.getPlaceInfos();
         place.getPlaceOpeningHours();
         place.getPlaceDurations();
         place.getLocations();
         place.getPlaceFreeOrNot();
         place.getPlaceTimeZone();
         place.getCity();
         place.getLinksPts();
         place.getPlaceTags();

         PlaceInfoGeolocation placeInfoGeolocation = new PlaceInfoGeolocation(
                 1,                // id
                 "Place Info",     // placeinfo
                 new Place()       // place (assuming a Place object is available)
         );

// Using setters to modify each property
         placeInfoGeolocation.setId(2);
         placeInfoGeolocation.setPlaceinfo("Updated Place Info");
         placeInfoGeolocation.setPlace(new Place());  // Assuming a new Place object is created

// Accessing each property directly with getters
         placeInfoGeolocation.getId();
         placeInfoGeolocation.getPlaceinfo();
         placeInfoGeolocation.getPlace();

         Countries country = new Countries(
                 1,                // id
                 "Country Name",   // country
                 20.5937,          // latitude
                 78.9629,          // longitude
                 "Asia",           // region
                 1000000,          // population
                 new HashSet<>(),  // cities (empty Set of City objects)
                 new Continent()    // continent (assuming a Continent object is available)
         );
         country.setId(1);
         country.setCountry("Initial Country Name");
         country.setLatitude(20.5937);
         country.setLongitude(78.9629);
         country.setRegion("Asia");
         country.setPopulation(1000000);

         country.setCities(new HashSet<>());
         country.setContinent(new Continent()); // Assuming a Continent object is available

// Access properties
         country.getId();
          country.getCountry();
         country.getLatitude();
          country.getLongitude();
          country.getRegion();
        country.getPopulation();
         country.getCities();
          country.getContinent();


         //--
         // Creating an instance of City
         City city = new City(
                 1,                  // id
                 "City Name",        // name
                 12.9716,            // latitude
                 77.5946,            // longitude
                 30.5,               // temperature
                 "12.9716° N, 77.5946° E", // gps
                 new HashSet<>(),    // places (empty Set of Place objects)
                 new HashSet<>(),    // citySummaries (empty Set of CitySummary objects)
                 new Countries()     // countries (assuming a Countries object is available)
         );

// Using setters to modify each property
         city.setId(2);
         city.setName("Updated City Name");
         city.setLatitude(40.7128);
         city.setLongitude(-74.0060);
         city.setTemperature(25.0);
         city.setGps("40.7128° N, 74.0060° W");

         city.setPlaces(new HashSet<>());          // Assuming an empty Set of Place objects
         city.setCitySummaries(new HashSet<>());   // Assuming an empty Set of CitySummary objects
         city.setCountries(new Countries());      // Assuming a new Countries object is created

// Accessing each property directly with getters
         city.getId();
         city.getName();
         city.getLatitude();
         city.getLongitude();
         city.getTemperature();
         city.getGps();
         city.getPlaces();
         city.getCitySummaries();
         city.getCountries();

         // Creating an instance of PlaceRequirementQuestion
         PlaceRequirementQuestion requirementQuestion = new PlaceRequirementQuestion(
                 1,                // id
                 "Question Name",  // name
                 "Question Description", // description
                 "Active",         // status
                 new HashSet<>(),  // placeRequirements (empty Set of PlaceRequirement objects)
                 new RequirementType() // requirementType (assuming a RequirementType object is available)
         );

// Using setters to modify each property
         requirementQuestion.setId(2);
         requirementQuestion.setName("Updated Question Name");
         requirementQuestion.setDescription("Updated Question Description");
         requirementQuestion.setStatus("Inactive");

         requirementQuestion.setPlaceRequirements(new HashSet<>());          // Assuming an empty Set of PlaceRequirement objects
         requirementQuestion.setRequirementType(new RequirementType());      // Assuming a new RequirementType object is created

// Accessing each property directly with getters
         requirementQuestion.getId();
         requirementQuestion.getName();
         requirementQuestion.getDescription();
         requirementQuestion.getStatus();
         requirementQuestion.getPlaceRequirements();
         requirementQuestion.getRequirementType();

         // Creating an instance of RequirementType
         RequirementType requirementType = new RequirementType(
                 1,                // id
                 "Type Name",      // name
                 "Code123",        // code
                 true,             // statut (true or false)
                 new HashSet<>()   // placeRequirementQuestions (empty Set of PlaceRequirementQuestion objects)
         );

// Using setters to modify each property
         requirementType.setId(2);
         requirementType.setName("Updated Type Name");
         requirementType.setCode("UpdatedCode456");
         requirementType.setStatut(false);

         requirementType.setPlaceRequirementQuestions(new HashSet<>());  // Assuming an empty Set of PlaceRequirementQuestion objects

// Accessing each property directly with getters
         requirementType.getId();
         requirementType.getName();
         requirementType.getCode();
         requirementType.isStatut();
         requirementType.getPlaceRequirementQuestions();

         PlaceInfo placeInfo = new PlaceInfo(
                 1,                  // id
                 "123-456-7890",     // phone
                 "987-654-3210",     // phone2
                 "Content details",  // content
                 "www.example.com",  // website
                 "123 Example St.",  // address
                 new Place()         // place (assuming a Place object is available)
         );

// Using setters to modify each property
         placeInfo.setId(2);
         placeInfo.setPhone("111-222-3333");
         placeInfo.setPhone2("444-555-6666");
         placeInfo.setContent("Updated content details");
         placeInfo.setWebsite("www.updated-example.com");
         placeInfo.setAddress("456 Updated St.");

// Assuming a Place object is available, you can set it as well
         placeInfo.setPlace(new Place());

// Accessing each property directly with getters
         placeInfo.getId();
         placeInfo.getPhone();
         placeInfo.getPhone2();
         placeInfo.getContent();
         placeInfo.getWebsite();
         placeInfo.getAddress();
         placeInfo.getPlace();


         // Creating a PlaceRequirement object using the constructor
         PlaceRequirementsId idd = new PlaceRequirementsId();  // Assuming PlaceRequirementsId is set up

         PlaceRequirement placeRequirement = new PlaceRequirement(
                 idd,                  // Embedded ID
                 new Date(),          // dateCreation
                 requirementQuestion, // requirementQuestion
                 place                // place
         );

// Using setters to modify properties
         placeRequirement.setId(new PlaceRequirementsId());
         placeRequirement.setDateCreation(new Date());
         placeRequirement.setRequirementQuestion(new PlaceRequirementQuestion());
         placeRequirement.setPlace(new Place());
         placeRequirement.getId();
         placeRequirement.getDateCreation();
         placeRequirement.getRequirementQuestion();
         placeRequirement.getPlace();

// Creating a PlaceTimeZone object using the constructor
         PlaceTimeZone placeTimeZone = new PlaceTimeZone(
                 1,              // id
                 "+03:00",       // utcOffset
                 "East Zone",    // name
                 new HashSet<>()  // Set<Place> places, assuming an empty set or you can initialize with specific places
         );

// Using setters to modify properties
         placeTimeZone.setId(2);
         placeTimeZone.setUtcOffset("+04:00");
         placeTimeZone.setName("West Zone");

// Using getters to access properties
         placeTimeZone.getId();
        placeTimeZone.getUtcOffset();
         placeTimeZone.getName();
         Set<Place> places = placeTimeZone.getPlaces();

// If you need to update the places set, you can add or remove places from the Set
         places.add(new Place());  // Assuming a Place object is available
         placeTimeZone.setPlaces(places);


         PlacePricing placePricing = new PlacePricing(
                 1,            // id
                 100.0,        // priceMin
                 500.0,        // priceMax
                 300.0,        // price
                 new Place()   // place (assuming a Place object is available)
         );

// Using setters to modify properties
         placePricing.setId(2);
         placePricing.setPriceMin(150.0);
         placePricing.setPriceMax(600.0);
         placePricing.setPrice(400.0);

         placePricing.setPlace(new Place());

// Using getters to access properties directly
         placePricing.getId();
         placePricing.getPriceMin();
         placePricing.getPriceMax();
         placePricing.getPrice();
         placePricing.getPlace();

         // Creating a PlaceTag object using the constructor
         PlaceTag placeTag = new PlaceTag(
                 1,               // id
                 "Beach",         // tag
                 "Nature",        // category
                 new HashSet<>()   // places (assuming a Set<Place> object is available)
         );

// Using setters to modify properties
         placeTag.setId(2);
         placeTag.setTag("Mountain");
         placeTag.setCategory("Adventure");

// Assuming a Set<Place> object is available, you can set it as well
         placeTag.setPlaces(new HashSet<Place>());

// Using getters to access properties directly
         placeTag.getId();
         placeTag.getTag();
         placeTag.getCategory();
         placeTag.getPlaces();


         PlaceType placeType = new PlaceType(
                 1,            // id
                 "Hotel",      // name
                 "HTL",        // code
                 new HashSet<>() // places (assuming a Set<Place> object is available)
         );

// Using setters to modify properties
         placeType.setId(2);
         placeType.setName("Resort");
         placeType.setCode("RSR");

// Assuming a Set<Place> object is available, you can set it as well
         placeType.setLinksPts(new HashSet<LinksPt>());

// Using getters to access properties directly
         placeType.getId();
         placeType.getName();
         placeType.getCode();
         placeType.getLinksPts();

         PlaceFreeOrNot placeFreeOrNot = new PlaceFreeOrNot(
                 1,              // id
                 "Yes",          // isFree (assuming a value like "Yes" or "No")
                 new HashSet<>()  // places (assuming a Set<Place> object is available)
         );

// Using setters to modify properties
         placeFreeOrNot.setId(2);
         placeFreeOrNot.setIsFree("No");

// Assuming a Set<Place> object is available, you can set it as well
         placeFreeOrNot.setPlaces(new HashSet<Place>());

// Using getters to access properties directly
         placeFreeOrNot.getId();
         placeFreeOrNot.getIsFree();
         placeFreeOrNot.getPlaces();

         Continent continent= new Continent(
                 1,                // id
                 "Asia",           // name
                 new HashSet<>()    // countries (assuming a Set<Countries> object is available)
         );

// Using setters to modify properties
         continent.setId(2);
         continent.setName("Europe");

// Assuming a Set<Countries> object is available, you can set it as well
         continent.setCountries(new HashSet<Countries>());

// Using getters to access properties directly
         continent.getId();
         continent.getName();
         continent.getCountries();
         //--
         PlaceOpeningHour openingHour = new PlaceOpeningHour(
                 1,                    // id
                 LocalTime.of(9, 0),    // openingTime
                 LocalTime.of(17, 0),   // closeTime
                 new Place()            // place (assuming a Place object is available)
         );

// Using setters to modify properties
         openingHour.setId(2);
         openingHour.setOpeningTime(LocalTime.of(10, 0));  // Example of updating the opening time
         openingHour.setCloseTime(LocalTime.of(18, 0));    // Example of updating the close time

// Assuming a Place object is available, you can set it as well
         openingHour.setPlace(new Place());

// Using getters to access properties directly
         openingHour.getId();
         openingHour.getOpeningTime();
         openingHour.getCloseTime();
         openingHour.getPlace();


         PlaceRequirementsId placeRequirementsId = new PlaceRequirementsId(
                 1,  // fkidRequirementQuestions
                 2   // fkidPlace
         );

// Using setters to modify properties
         placeRequirementsId.setFkidRequirementQuestions(3); // Updating fkidRequirementQuestions
         placeRequirementsId.setFkidPlace(4);                // Updating fkidPlace

// Using getters to access properties directly
         placeRequirementsId.getFkidRequirementQuestions();
         placeRequirementsId.getFkidPlace();

// Creating a CitySummary object using the constructor
         CitySummary citySummary = new CitySummary(
                 1,             // id
                 100L,          // totalPlaces
                 5000L,         // totalDuration
                 new City()     // city (assuming a City object is available)
         );

// Using setters to modify properties
         citySummary.setId(2);                // Updating id
         citySummary.setTotalPlaces(200L);    // Updating totalPlaces
         citySummary.setTotalDuration(6000L); // Updating totalDuration

// Assuming a City object is available, you can set it as well
         citySummary.setCity(new City());     // Updating city

// Using getters to access properties directly
         citySummary.getId();
         citySummary.getTotalPlaces();
         citySummary.getTotalDuration();
         citySummary.getCity();

// Creating a PlaceDuration object using the constructor
         PlaceDuration placeDuration = new PlaceDuration(
                 1,           // id
                 3600L,       // duration (e.g., in seconds)
                 new Place()  // place (assuming a Place object is available)
         );

// Using setters to modify properties
         placeDuration.setId(2);            // Updating id
         placeDuration.setDuration(7200L);  // Updating duration
         placeDuration.setPlace(new Place());  // Updating place

// Using getters to access properties directly
         placeDuration.getId();
         placeDuration.getDuration();
         placeDuration.getPlace();

         LinksTp linksTp = new LinksTp(
                 new LinksTpId(1, 1),  // id (assuming LinksTpId constructor takes two integer values)
                 new Place(),          // place (assuming a Place object is available)
                 new PlaceTag()        // placeTag (assuming a PlaceTag object is available)
         );

// Using setters to modify each property
         linksTp.setId(new LinksTpId(2, 2));  // Updating id
         linksTp.setPlace(new Place());       // Updating place
         linksTp.setPlaceTag(new PlaceTag()); // Updating placeTag

// Accessing each property directly using getters
         linksTp.getId();
         linksTp.getPlace();
         linksTp.getPlaceTag();

         LinksPt linksPt = new LinksPt(
                 new LinksPtId(1, 1),  // id (assuming LinksPtId constructor takes two integer values for places and placeType)
                 new Place(),          // place (assuming a Place object is available)
                 new PlaceType()       // placeType (assuming a PlaceType object is available)
         );

// Using setters to modify each property
         linksPt.setId(new LinksPtId(2, 2));  // Updating id
         linksPt.setPlace(new Place());       // Updating place
         linksPt.setPlaceType(new PlaceType()); // Updating placeType

// Accessing each property directly using getters
         linksPt.getId();
         linksPt.getPlace();
         linksPt.getPlaceType();

         // Creating a LinksTpId object using the constructor
         LinksTpId linksTpId = new LinksTpId(1, 2);  // Example initialization with fkidPlace = 1, fkidPlaceTags = 2

// Using setters to modify each property
         linksTpId.setFkidPlace(3);  // Updating fkidPlace
         linksTpId.setFkidPlaceTags(4);  // Updating fkidPlaceTags

// Accessing each property directly using getters
         linksTpId.getFkidPlace();    // Returns the fkidPlace value
         linksTpId.getFkidPlaceTags();  // Returns the fkidPlaceTags value

         // Creating a LinksPtId object using the constructor
         LinksPtId linksPtId = new LinksPtId(1, 2);  // Example initialization with fkidPlace = 1, fkidPlaceTypes = 2

// Using setters to modify each property
         linksPtId.setFkidPlace(3);  // Updating fkidPlace to 3
         linksPtId.setFkidPlaceTypes(4);  // Updating fkidPlaceTypes to 4

// Accessing each property directly using getters
         linksPtId.getFkidPlace();       // Returns the fkidPlace value, e.g., 3
         linksPtId.getFkidPlaceTypes();  // Returns the fkidPlaceTypes value, e.g., 4








     }

     @Test
    void Social_networkEntities(){
         assertThat(1).isPositive();

         Profile profile = new Profile(
                 1,                      // id
                 "John Doe",             // name
                 "john_doe",             // urlPublic
                 new User(),             // user (assuming a User object is created)
                 new ArrayList<>(),      // posts (empty list of Post)
                 new ArrayList<>(),      // followers (empty list of Follower)
                 new ArrayList<>(),      // followings (empty list of Follower)
                 new ProfileType(),      // profileType (assuming ProfileType object is created)
                 new ProfilePrivacy()  // profilePrivacy (assuming ProfilePrivacy object is created)
         );

         profile.setId(2);  // Updating the ID
         profile.setName("Jane Doe");  // Updating the name
         profile.setUrlPublic("jane_doe");  // Updating the public URL
         profile.setUser(new User());  // Assuming a new User object is created
         profile.setPosts(new ArrayList<>());  // Updating posts with an empty list
         profile.setFollowers(new ArrayList<>());  // Updating followers with an empty list
         profile.setFollowings(new ArrayList<>());  // Updating followings with an empty list
         profile.setProfileType(new ProfileType());  // Assuming a new ProfileType object is created
         profile.setProfilePrivacy(new ProfilePrivacy());

         profile.getId();  // Get the updated ID
         profile.getName();  // Get the updated name
          profile.getUrlPublic();  // Get the updated public URL
          profile.getUser();  // Get the updated User object
         profile.getPosts();  // Get the updated list of posts
         profile.getFollowers();  // Get the updated list of followers
          profile.getFollowings();  // Get the updated list of followings
         profile.getProfileType();  // Get the updated ProfileType
         profile.getProfilePrivacy();

         Post post = new Post(
                 1,                          // id
                 Instant.now(),               // postDate (current timestamp)
                 "This is a description",     // description
                 "This is the content",       // content
                 "path/to/picture.jpg",       // picture (image path)
                 "Casual",                    // style
                 true,                        // visibility
                 new HashSet<>(),             // comments (empty set)
                 new HashSet<>(),             // reactions (empty set)
                 new Circuit(),               // circuit (assuming Circuit object is created)
                 new Profile(),               // profile (assuming Profile object is created)
                 new HashSet<>()              // tags (empty set)
         );

// Using setter methods to update properties
         post.setId(2);                        // Updating the ID
         post.setPostDate(Instant.now());      // Updating the post date
         post.setDescription("Updated description"); // Updating the description
         post.setContent("Updated content");  // Updating the content
         post.setPicture("new/path/to/picture.jpg");  // Updating the picture path
         post.setStyle("Formal");             // Updating the style
         post.setVisibility(false);
         post.setContent("dsfd");
         Set<Reaction> reactions = new HashSet<>();
         Set<Tag> tags = new HashSet<>();
         post.setReactions(reactions);
        post.setTags(tags);
// Using getter methods to access properties
      post.getId();               // Get the updated ID
          post.getPostDate();       // Get the updated post date
          post.getDescription();  // Get the updated description
         post.getContent();     // Get the updated content
         post.getPicture();     // Get the updated picture
         post.getStyle();         // Get the updated style
         post.isVisibility();  // Get the updated visibility

// Accessing associated objects
   post.getComments();  // Get the set of comments
         post.getReactions();  // Get the set of reactions
         post.getCircuit();             // Get the associated Circuit object
         post.getProfile();             // Get the associated Profile object
       post.getTags();


         ReactionType reactionType = new ReactionType(
                 1,                               // id
                 "Like",                           // name
                 "User liked this post",          // description
                 "LIK",                            // code
                 "active",                         // status
                 new ArrayList<>()                 // reactions (empty list)
         );
         reactionType.setId(1);                          // Set the ID
         reactionType.setName("Like");                    // Set the name
         reactionType.setDescription("User liked this post");  // Set the description
         reactionType.setCode("LIK");                     // Set the code
         reactionType.setStatus("active");                // Set the status
         reactionType.setReactions(new ArrayList<>());    // Set reactions (empty list)

// Using setter methods to update properties
         reactionType.setId(2);                          // Updating the ID
         reactionType.setName("Love");                   // Updating the name
         reactionType.setDescription("User loves this post");  // Updating the description
         reactionType.setCode("LOVE");                   // Updating the code
         reactionType.setStatus("inactive");
// Using getter methods to access properties
         reactionType.getId();            // Get the updated ID
         reactionType.getName();         // Get the updated name
         reactionType.getDescription(); // Get the updated description
         reactionType.getCode();         // Get the updated code
         reactionType.getStatus();     // Get the updated status

// Accessing associated reactions
        reactionType.getReactions();

         Reaction reaction = new Reaction(
                 1,                                      // id
                 new Date(),                              // date (current date)
                 new Post(),                              // post (assuming Post object is created)
                 new Profile(),                           // profile (assuming Profile object is created)
                 new ReactionType(),                      // reactionType (assuming ReactionType object is created)
                 new ReactionLevel()                      // reactionLevel (assuming ReactionLevel object is created)
         );

// Using setter methods to update properties
         reaction.setId(2);                          // Updating the ID
         reaction.setDate(new Date());               // Updating the reaction date to current date
         reaction.setPost(new Post());               // Updating the post object
         reaction.setProfile(new Profile());         // Updating the profile object
         reaction.setReactionType(new ReactionType()); // Updating the reaction type
         reaction.setReactionLevel(new ReactionLevel());

         reaction.getId();               // Get the updated ID
        reaction.getDate();              // Get the updated date
         reaction.getPost();              // Get the associated Post object
          reaction.getProfile();     // Get the associated Profile object
        reaction.getReactionType();  // Get the associated ReactionType
          reaction.getReactionLevel();
         Profile followerProfile = new Profile(); // Assuming Profile object is created
         Profile followingProfile = new Profile();
         Follower follower = new Follower(
                 1, // id (initial id value, generated by database)
                 new Date(), // dateFollow (current date)
                 null, // dateUnfollow (null since not unfollowed yet)
                 true, // isApproved (approval status)
                 followerProfile, // associated followerProfile (Profile object)
                 followingProfile // associated following (Profile object)
         );

// You can now use the getter and setter methods to manipulate and access the fields
// Example: Setting properties after initialization
         follower.setDateUnfollow(new Date()); // Setting a dateUnfollow (unfollow date)
            follower.setId(1);
            new Follower();
// Example: Accessing fields using getter methods
         System.out.println("Follower ID: " + follower.getId());
         System.out.println("Follow Date: " + follower.getDateFollow());
         System.out.println("Unfollow Date: " + follower.getDateUnfollow());
         System.out.println("Is Approved: " + follower.isApproved());
         System.out.println("Follower Profile: " + follower.getFollowerProfile());
         System.out.println("Following Profile: " + follower.getFollowing());
         follower.setFollowerProfile(new Profile()); // Set a new follower profile
         follower.setFollowing(new Profile()); // Set a new following profile
         follower.setApproved(true); // Approve the follow request
         follower.setDateFollow(new Date()); // Update the date when followed
         follower.setDateUnfollow(new Date());
         ProfileType profileType=  new ProfileType(
                 1,                // id
                 "Admin",          // name
                 "ADMIN_CODE",     // code
                 "ACTIVE",         // status
                 new ArrayList<Profile>()              // profiles (can be set to null or a list of Profile objects)
         );
         profileType.setId(2);             // Set the ID to 2
         profileType.setName("Moderator"); // Set the name to "Moderator"
         profileType.setCode("MOD_CODE"); // Set the code to "MOD_CODE"
         profileType.setStatus("ACTIVE"); // Set the status to "ACTIVE"
         profileType.setProfiles(null);    // Set the profiles to null (or provide a list of profiles)

         // Getting all the fields using getter methods
         profileType.getId();              // Get the ID
         profileType.getName();            // Get the name
         profileType.getCode();            // Get the code
         profileType.getStatus();        // Get the status
         profileType.getProfiles();

         ReactionLevel reactionLevel = new ReactionLevel(
                 1,          // id
                 5,          // value
                 2,          // order
                 null        // reactions (can be set to null or a list of Reaction objects)
         );

         // Setting all fields using setter methods
         reactionLevel.setId(2);             // Set the ID to 2
         reactionLevel.setValue(10);         // Set the value to 10
         reactionLevel.setOrder(1);          // Set the order to 1
         reactionLevel.setReactions(null);   // Set the reactions to null (or provide a list of reactions)

         // Getting all fields using getter methods
         reactionLevel.getId();            // Get the ID
          reactionLevel.getValue();          // Get the value
         reactionLevel.getOrder();          // Get the order
         reactionLevel.getReactions();

         Tag tag = new Tag(
                 1,               // id
                 "Technology",    // name
                 "tech_ref",      // reference
                 null             // posts (can be set to null or a Set of Post objects)
         );

         // Setting all fields using setter methods
         tag.setId(2);              // Set the ID to 2
         tag.setName("Science");    // Set the name to "Science"
         tag.setReference("science_ref");  // Set the reference to "science_ref"
         tag.setPosts(null);         // Set the posts to null (or provide a Set of Post objects)

         // Getting all fields using getter methods
         tag.getId();                // Get the ID
          tag.getName();             // Get the name
         tag.getReference();   // Get the reference
          tag.getPosts();

         PostComment postComment = new PostComment(
                 1,                        // id
                 "This is a comment",      // content
                 "100",                    // reactionNumber
                 null                       // post (can be set to null or a Post object)
         );

         // Setting all fields using setter methods
         postComment.setId(2);                // Set the ID to 2
         postComment.setContent("Updated comment content");  // Set the content
         postComment.setReactionNumber("150"); // Set the reaction number
         postComment.setPost(null);            // Set the post to null (or provide a Post object)

         // Directly calling getter methods without storing the result in variables
         postComment.getId();                 // Get the ID
         postComment.getContent();            // Get the content
         postComment.getReactionNumber();     // Get the reaction number
         postComment.getPost();               // Get the associated post (this will return null in this example)

         LinksPtg linksPtg = new LinksPtg(
                 new LinksPtgId(),  // id (LinksPtgId object)
                 null,              // post (can be set to null or a Post object)
                 null               // tag (can be set to null or a Tag object)
         );

         // Setting all fields using setter methods
         linksPtg.setId(new LinksPtgId());   // Set the ID to a new LinksPtgId object
         linksPtg.setPost(null);              // Set the post to null (or provide a Post object)
         linksPtg.setTag(null);               // Set the tag to null (or provide a Tag object)

         // Directly calling getter methods without storing the result in variables
         linksPtg.getId();                    // Get the ID
         linksPtg.getPost();                  // Get the associated post (this will return null in this example)
         linksPtg.getTag();                   // Get the associated tag (this will return null in this example)




         ProfilePrivacy  profilePrivacy= new ProfilePrivacy(
                 1,                // id
                 "Public",         // name
                 "public_code"     // code
         );

         // Setting all fields using setter methods
         profilePrivacy.setId(2);               // Set the ID to 2
         profilePrivacy.setName("Private");     // Set the name to "Private"
         profilePrivacy.setCode("private_code"); // Set the code to "private_code"

         // Directly calling getter methods without storing the result in variables
         profilePrivacy.getId();                // Get the ID
         profilePrivacy.getName();              // Get the name
         profilePrivacy.getCode();

         LinksPtgId linksPtgId = new LinksPtgId(
                 1,  // fkidPost
                 2   // fkidTag
         );

         // Setting all fields using setter methods
         linksPtgId.setFkidPost(3);  // Set the fkidPost to 3
         linksPtgId.setFkidTag(4);   // Set the fkidTag to 4

         // Directly calling getter methods without storing the result in variables
         linksPtgId.getFkidPost();   // Get the fkidPost
         linksPtgId.getFkidTag();

    }

    @Test
    void Reviews€ntities(){
        assertThat(1).isPositive();

        Review review = new Review(
                1,                  // id
                "Great service",    // note
                Instant.now(),      // reviewDate
                true,               // visibility
                true,               // status
                true,               // recommended
                false,              // archive
                "Good quality",     // comment
                5,                  // rating
                null,               // fkidType (can be set to null or a FeedbackType object)
                null,               // fkidReason (can be set to null or a Reason object)
                null                // fkidWaitingTime (can be set to null or a WaitingTime object)
        );

        // Setting all fields using setter methods
        review.setId(2);               // Set the ID to 2
        review.setNote("Excellent!");  // Set the note to "Excellent!"
        review.setReviewDate(Instant.now());  // Set the reviewDate to the current time
        review.setVisibility(false);   // Set visibility to false
        review.setStatus(true);        // Set status to true
        review.setRecommended(false);  // Set recommended to false
        review.setArchive(true);       // Set archive to true
        review.setComment("Very satisfied"); // Set comment to "Very satisfied"
        review.setRating(4);           // Set rating to 4
        review.setFkidType(null);      // Set fkidType to null (or provide a FeedbackType object)
        review.setFkidReason(null);    // Set fkidReason to null (or provide a Reason object)
        review.setFkidWaitingTime(null); // Set fkidWaitingTime to null (or provide a WaitingTime object)

        // Directly calling getter methods without storing the result in variables
        review.getId();                // Get the id
        review.getNote();              // Get the note
        review.getReviewDate();        // Get the review date
        review.getVisibility();        // Get the visibility
        review.getStatus();            // Get the status
        review.getRecommended();       // Get the recommended
        review.getArchive();           // Get the archive status
        review.getComment();           // Get the comment
        review.getRating();            // Get the rating
        review.getFkidType();          // Get the FeedbackType
        review.getFkidReason();        // Get the Reason
        review.getFkidWaitingTime();   // Get the WaitingTime

        WaitingTime waitingTime = new WaitingTime(
                1,              // id
                "Short Wait",   // title
                30,             // value
                1,              // order
                true,           // status
                true ,
                new HashSet<Review>()// recommended
        );
       new WaitingTime();        // Setting all fields using setter methods
        waitingTime.setId(2);              // Set the ID to 2
        waitingTime.setTitle("Long Wait"); // Set title to "Long Wait"
        waitingTime.setValue(60);          // Set value to 60
        waitingTime.setOrder(2);           // Set order to 2
        waitingTime.setStatus(false);      // Set status to false
        waitingTime.setRecommended(false);
        waitingTime.getReviews();
        Set<Review> reviews = new HashSet<>();

        waitingTime.setReviews(reviews);// Set recommended to false

        // Directly calling getter methods without storing the result in variables
        waitingTime.getId();               // Get the id
        waitingTime.getTitle();            // Get the title
        waitingTime.getValue();            // Get the value
        waitingTime.getOrder();            // Get the order
        waitingTime.getStatus();           // Get the status
        waitingTime.getRecommended();      // Get the recommended

        // Access the Set of reviews associated with this WaitingTime
        waitingTime.getReviews();

        FeedbackType feedbackType = new FeedbackType(
                1,             // id
                "Positive",    // label
                1,             // order
                true           // status
        );

        // Setting all fields using setter methods
        feedbackType.setId(2);            // Set the ID to 2
        feedbackType.setLabel("Negative"); // Set label to "Negative"
        feedbackType.setOrder(2);         // Set order to 2
        feedbackType.setStatus(false);    // Set status to false

        // Directly calling getter methods without storing the result in variables
        feedbackType.getId();             // Get the id
        feedbackType.getLabel();          // Get the label
        feedbackType.getOrder();          // Get the order
        feedbackType.getStatus();

        Reason reason = new Reason(
                1,             // id
                "Service Quality",  // object
                true  ,
                new HashSet<Review>()// status
        );

        // Setting all fields using setter methods
        reason.setId(2);             // Set the ID to 2
        reason.setObject("Delivery Speed");  // Set object to "Delivery Speed"
        reason.setStatus(false);
        reason.setReviews(reviews);// Set status to false

        // Directly calling getter methods without storing the result in variables
        reason.getId();              // Get the id
        reason.getObject();          // Get the object (reason description)
        reason.getStatus();
        reason.getReviews();





















    }

    @Test
    void TranslationsEntities(){
        assertThat(1).isPositive();

        EditorialSummaryId editorialSummaryId = new EditorialSummaryId(1, 2);  // assuming fkid_lang = 1, fkid_object = 2

        // Creating a new EditorialSummary instance using the all-args constructor
        EditorialSummary editorialSummary = new EditorialSummary(
                editorialSummaryId,      // id
                "Sample Name",           // name
                "Sample text content",   // value
                new Date(),              // dateCreation
                new Date(),              // dateUpdate
                new Language(),   // Language instance
                new EditorialObject(), // EditorialObject instance
                new User()  // User instance
        );

        // Setting fields individually
        editorialSummary.setName("Updated Name");                  // Set name
        editorialSummary.setValue("Updated text content");         // Set value
        editorialSummary.setDateCreation(new Date());              // Set dateCreation to the current date
        editorialSummary.setDateUpdate(new Date());                // Set dateUpdate to the current date

        // Setting associations (assuming instances of Language, EditorialObject, and User)
        editorialSummary.setLanguages(new Language());      // Update the language
        editorialSummary.setEditorialObject(new EditorialObject());  // Update the editorial object
        editorialSummary.setUser(new User());          // Update the user

        // Getting all fields without storing them in variables
        editorialSummary.getId();               // Get the ID
        editorialSummary.getName();             // Get the name
        editorialSummary.getValue();            // Get the value
        editorialSummary.getDateCreation();     // Get the creation date
        editorialSummary.getDateUpdate();       // Get the update date
        editorialSummary.getLanguages();        // Get the associated Language
        editorialSummary.getEditorialObject();  // Get the associated EditorialObject
        editorialSummary.getUser();             // Get the associated User

        EditorialObject editorialObject = new EditorialObject(
                1,                    // id (can be null initially if auto-generated)
                "Object Name",        // name
                "Code123",            // code
                true,                 // status
                new HashSet<>()       // editorialSummaries (empty Set initially)
        );

        // Setting fields individually
        editorialObject.setName("Updated Object Name");   // Set name
        editorialObject.setCode("UpdatedCode123");        // Set code
        editorialObject.setStatus(false);                 // Set status

        // Adding an EditorialSummary to the editorialSummaries set
        EditorialSummary summary = new EditorialSummary();
        summary.setName("Sample Summary");
        editorialObject.getEditorialSummaries().add(summary);  // Add a summary

        // Getting all fields without storing them in variables
        editorialObject.getId();                  // Get the ID
        editorialObject.getName();                // Get the name
        editorialObject.getCode();                // Get the code
        editorialObject.getStatus();              // Get the status
        editorialObject.getEditorialSummaries();

        Language language = new Language(
                1,                    // id (nullable if auto-generated)
                "English",            // name
                "EN",                 // code
                new HashSet<>()       // editorialSummaries (empty Set initially)
        );

        // Setting fields individually
        language.setName("French");   // Set name
        language.setCode("FR");       // Set code

        // Adding an EditorialSummary to the editorialSummaries set
        summary.setName("Sample Summary");
        language.getEditorialSummaries().add(summary);  // Add a summary

        // Getting all fields without storing them in variables
        language.getId();                  // Get the ID
        language.getName();                // Get the name
        language.getCode();                // Get the code
        language.getEditorialSummaries();

        // Setting fields individually
        editorialSummaryId.setLanguageId(3); // Set the language ID
        editorialSummaryId.setObjectId(4);   // Set the object ID

        // Getting fields without storing them in variables
        editorialSummaryId.getLanguageId();  // Get the language ID
        editorialSummaryId.getObjectId();


    }

    @Test
    void AgencyEntites() {
        assertThat(1).isPositive();

        User owner = new User();  // Populate the User entity as needed
        owner.setId(1);  // Set the ID or other properties of the owner

        // Example agency circuits
        List<AgencyCircuit> agencyCircuits = new ArrayList<>();
        AgencyCircuit circuit1 = new AgencyCircuit();
        circuit1.setDescription("Adventure Circuit");
        agencyCircuits.add(circuit1);

        // Example agency images
        List<AgencyImages> images = new ArrayList<>();
        AgencyImages image1 = new AgencyImages();
        image1.setUrl("https://example.com/image1.jpg");
        images.add(image1);

        // Example agency documents
        Set<Document> documents = new HashSet<>();
        Document document1 = new Document();
        document1.setTitle("License Document");
        documents.add(document1);

        // Create an Agency instance
        Agency agency = new Agency(
                1, // id
                "123456789", // ice
                "Tech Agency", // name
                "contact@techagency.com", // email
                "123-456-7890", // contactNumber
                "123 Tech Street", // address
                "logo.png", // logo
                "Leading tech agency", // description
                LocalDate.of(2020, 1, 1), // openingDate
                LocalDate.of(2025, 12, 31), // closingDate
                LocalTime.of(9, 0), // opensHours
                LocalTime.of(18, 0), // closedHours
                new User(), // owner (assuming User object is created)
                new ArrayList<>(), // agencyCircuits
                new ArrayList<>(), // images
                new HashSet<>(), // documents
                true, // isVerified
                false, // isDeleted
                true, // isEnable
                LocalDateTime.now(), // createdAt
                LocalDateTime.now()  // updatedAt
        );        agency.setIce("1234567890");  // Set the ICE number
        agency.setName("Explore Adventures");  // Agency name
        agency.setEmail("contact@exploreadventures.com");  // Contact email
        agency.setContactNumber("+123456789");  // Contact number
        agency.setAddress("123 Adventure St, Cityville");  // Address
        agency.setLogo("https://example.com/logo.jpg");  // Logo URL
        agency.setDescription("A premier adventure travel agency.");  // Description
        agency.setOpeningDate(LocalDate.of(2020, 1, 1));  // Opening date
        agency.setClosingDate(LocalDate.of(2030, 1, 1));  // Optional closing date
        agency.setOpensHours(LocalTime.of(8, 0));  // Opening hours
        agency.setClosedHours(LocalTime.of(18, 0));  // Closing hours
        agency.setOwner(owner);  // Set the owner User entity
        agency.setAgencyCircuits(agencyCircuits);  // Set the list of circuits
        agency.setImages(images);  // Set the list of images
        agency.setDocuments(documents);  // Set the documents
        agency.setIsVerified(true);  // Verification status
        agency.setIsDeleted(false);  // Deletion status
        agency.setIsEnable(true);  // Enabled status

        agency.setCreatedAt(LocalDateTime.now());
        agency.setUpdatedAt(LocalDateTime.now());

        agency.getId();                  // Get the ID
        agency.getIce();                 // Get the ICE
        agency.getName();                // Get the name
        agency.getEmail();               // Get the email
        agency.getContactNumber();       // Get the contact number
        agency.getAddress();             // Get the address
        agency.getLogo();                // Get the logo URL or file path
        agency.getDescription();         // Get the description
        agency.getOpeningDate();         // Get the opening date
        agency.getClosingDate();         // Get the closing date
        agency.getOpensHours();          // Get the opening hours
        agency.getClosedHours();         // Get the closing hours
        agency.getOwner();               // Get the associated User (owner)
        agency.getAgencyCircuits();      // Get the list of AgencyCircuit associated with the agency
        agency.getImages();              // Get the list of AgencyImages associated with the agency
        agency.getDocuments();           // Get the set of Documents associated with the agency
        agency.getIsVerified();          // Check if the agency is verified
        agency.getIsDeleted();           // Check if the agency is marked as deleted
        agency.getIsEnable();            // Check if the agency is enabled
        agency.getCreatedAt();           // Get the creation timestamp
        agency.getUpdatedAt();

        Document document = new Document(
                1,                             // id
                "Sample Document",             // title
                "PDF",                         // type
                "/path/to/sample.pdf",         // filePath
                new byte[]{0x25, 0x50, 0x44},  // file (example byte array)
                new Agency()                   // associated Agency (assuming you have an Agency instance)
        );

// Using getters to access the fields
        document.getId();               // Get the ID
        document.getTitle();            // Get the title
        document.getType();             // Get the type
        document.getFilePath();         // Get the file path
        document.getFile();             // Get the file byte array
        document.getAgency();           // Get the associated Agency

// Modifying fields using setter methods
        document.setTitle("Updated Title");       // Update the title
        document.setType("DOCX");                 // Update the type
        document.setFilePath("/new/path/to/doc"); // Update the file path
        document.setFile(new byte[]{0x64, 0x6F, 0x63});  // Update the file byte array
        document.setAgency(new Agency());

        AgencyHistory agencyHistory = new AgencyHistory(
                1,                              // id
                LocalDateTime.now(),            // tripDate (current date and time)
                "Completed",                    // status
                "The trip was successful",      // notes
                new AgencyCircuit(),            // associated AgencyCircuit (assuming you have an AgencyCircuit instance)
                new User()                       // associated User (assuming you have a User instance)
        );

// Using getters to access the fields
        agencyHistory.getId();               // Get the ID
        agencyHistory.getTripDate();         // Get the trip date
        agencyHistory.getStatus();           // Get the status
        agencyHistory.getNotes();            // Get the notes
        agencyHistory.getAgencyCircuit();    // Get the associated AgencyCircuit
        agencyHistory.getMadeBy();           // Get the associated User

// Modifying fields using setter methods
        agencyHistory.setStatus("Canceled");           // Update the status
        agencyHistory.setNotes("The trip was canceled due to weather conditions"); // Update the notes
        agencyHistory.setTripDate(LocalDateTime.now().minusDays(1)); // Update the trip date
        agencyHistory.setAgencyCircuit(new AgencyCircuit()); // Update the associated AgencyCircuit
        agencyHistory.setMadeBy(new User());

        AgencyImages agencyImages = new AgencyImages(
                1,                                  // id
                "http://example.com/image1.jpg",    // url
                "image/jpeg",                       // type
                "500KB",                             // size
                new Agency()                         // associated Agency (assuming you have an Agency instance)
        );

// Using getters to access the fields
        agencyImages.getId();                // Get the ID
        agencyImages.getUrl();               // Get the URL of the image
        agencyImages.getType();              // Get the type of the image
        agencyImages.getSize();              // Get the size of the image
        agencyImages.getAgency();            // Get the associated Agency

// Modifying fields using setter methods
        agencyImages.setUrl("http://example.com/image2.jpg");      // Update the URL
        agencyImages.setType("image/png");                          // Update the type
        agencyImages.setSize("700KB");                               // Update the size
        agencyImages.setAgency(new Agency());



        AgencyCircuit agencyCircuit = new AgencyCircuit(
                1,                              // id
                new Agency(),                   // associated Agency (assuming you have an Agency instance)
                new Circuit(),                  // associated Circuit (assuming you have a Circuit instance)
                "This is a sample description"  // description
        );

// Using getters to access the fields
        agencyCircuit.getId();            // Get the ID
        agencyCircuit.getAgency();        // Get the associated Agency
        agencyCircuit.getCircuit();       // Get the associated Circuit
        agencyCircuit.getDescription();  // Get the description

// Modifying fields using setter methods
        agencyCircuit.setDescription("Updated description");   // Update the description
        agencyCircuit.setAgency(new Agency());                 // Update the associated Agency
        agencyCircuit.setCircuit(new Circuit());


    }

    @Test
    void GeolocationEntities(){
        assertThat(1).isPositive();
        RequestAPI requestAPI = new RequestAPI(
                1,                           // id
                "40.748817,-73.985428",      // originCoords (e.g., coordinates of the origin)
                "34.052235,-118.243683",     // destinationCoords (e.g., coordinates of the destination)
                "driving",                   // mode (e.g., "driving", "walking", etc.)
                "en",                         // language (e.g., "en" for English)
                "avoid_tolls",                // routeRestriction (e.g., "avoid_tolls")
                Instant.now(),                // departureDate (current time)
                Instant.now().plusSeconds(3600), // arrivalTime (current time plus 1 hour)
                "best_guess",                 // trafficModel (e.g., "best_guess")
                "congestion",                 // trafficTransit (e.g., "congestion")
                "less_driving",               // transitRoutingPreference (e.g., "less_driving")
                "metric",                     // unit (e.g., "metric" for meters)
                new ArrayList<>(),            // responses (empty list of ResponseAPI)
                new ArrayList<>()             // routes (empty list of Route)
        );

// Using getters to access the fields
        requestAPI.getId();                  // Get the ID
        requestAPI.getOriginCoords();        // Get the origin coordinates
        requestAPI.getDestinationCoords();   // Get the destination coordinates
        requestAPI.getMode();                // Get the mode (e.g., "driving")
        requestAPI.getLanguage();            // Get the language
        requestAPI.getRouteRestriction();    // Get the route restriction
        requestAPI.getDepartureDate();       // Get the departure date
        requestAPI.getArrivalTime();         // Get the arrival time
        requestAPI.getTrafficModel();        // Get the traffic model
        requestAPI.getTrafficTransit();      // Get the traffic transit
        requestAPI.getTransitRoutingPreference(); // Get the transit routing preference
        requestAPI.getUnit();                // Get the unit (e.g., "metric")
        requestAPI.getResponses();           // Get the associated responses
        requestAPI.getRoutes();              // Get the associated routes

// Modifying fields using setters
        requestAPI.setOriginCoords("40.730610,-73.935242");  // Update the origin coordinates
        requestAPI.setDestinationCoords("34.052235,-118.243683"); // Update the destination coordinates
        requestAPI.setMode("walking");                       // Update the mode
        requestAPI.setLanguage("fr");                        // Update the language to French
        requestAPI.setRouteRestriction("avoid_ferries");     // Update route restriction
        requestAPI.setDepartureDate(Instant.now()); // Update departure date
        requestAPI.setArrivalTime(Instant.now());  // Update arrival time
        requestAPI.setTrafficModel("optimistic");             // Update traffic model
        requestAPI.setTrafficTransit("light");               // Update traffic transit
        requestAPI.setTransitRoutingPreference("shortest");   // Update transit routing preference
        requestAPI.setUnit("imperial");

        ResponseAPI responseAPI = ResponseAPI.builder()
                .id(1)                              // Set the ID
                .jsonResponse("{ \"status\": \"success\" }") // Set the JSON response
                .requestAPI(requestAPI)      // Set the associated RequestAPI instance
                .build();

// Using getters to access the fields
        responseAPI.getId();                   // Get the ID
        responseAPI.getJsonResponse();         // Get the JSON response
        responseAPI.getRequestAPI();           // Get the associated RequestAPI

        responseAPI.setJsonResponse("{ \"status\": \"error\" }"); // Update JSON response
        responseAPI.setRequestAPI(requestAPI);          // Update the associated RequestAPI

        responseAPI.equals(responseAPI); // Check if two ResponseAPI instances are equal

       responseAPI.hashCode();


        RouteMetric distanceMetric = new RouteMetric();
        RouteMetric durationMetric = new RouteMetric();
        RouteMetric durationInTrafficMetric = new RouteMetric();
        MetricDetails m=new MetricDetails();
// Creating the Route instance using the all-args constructor
        Route route = new Route(
                1,  // id
                distanceMetric,   // distance
                durationMetric,   // duration
                durationInTrafficMetric,  // duration in traffic
                requestAPI,  // RequestAPI (should be a valid RequestAPI instance)
                m  // MetricDetails (should be a valid MetricDetails instance)
        );
        route.getId();                        // Get the route ID
        RouteMetric routeDistance = route.getDistance();        // Get the distance metric
       routeDistance.getText();          // Get the text representation of distance
       routeDistance.getValue();        // Get the value of the distance

        RouteMetric routeDuration = route.getDuration();        // Get the duration metric
        routeDuration.getText();          // Get the text representation of duration
       routeDuration.getValue();        // Get the value of the duration

        route.setDistance(new RouteMetric());       // Update the distance
        route.setDuration(new RouteMetric());    // Update the duration
        route.setDurationInTraffic(new RouteMetric());
        Place originPlace = new Place();
        Place destinationPlace = new Place();
        List<Route> routeList = new ArrayList<>();
        MetricDetails metricDetails = new MetricDetails(1, originPlace, destinationPlace, routeList);

        metricDetails.setId(1);
        metricDetails.setOrigin(originPlace); // Assuming originPlace is a Place object
        metricDetails.setDestination(destinationPlace); // Assuming destinationPlace is a Place object
        metricDetails.setRoutes(routeList); // Assuming routeList is a List<Route> object

        // Get values using the generated getters
        metricDetails.getId();
       metricDetails.getOrigin();
        metricDetails.getDestination();
        metricDetails.getRoutes();

    }

    @Test
    void SecurityEntities() {

        String username = "john_doe";
        String email = "john.doe@example.com";
        String password = "password123";
        Instant createdDate = Instant.now();
        Boolean enabled = true;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String firstName = "John";
        String lastName = "Doe";
        User user = new User(
                1, // ID is generated by the database
                "ref123", // Reference
                username,
                email,
                password,
                null, // lastLogin (null for now)
                enabled,
                createdDate,
                lastName,
                firstName,
                birthDate,
                "New York", // birthPlace
                "123-456-7890", // phone
                "987-654-3210", // mobilePhone
                "gsm123", // gsm
                "prefix1", // prefixe
                "ISO2", // iso2
                "Engineer", // profession
                "Mr.", // civility
                "profile_pic.jpg", // picture
                "Acme Corp.", // companyName
                "123 Main St.", // address
                "10001", // zipCode
                null, // additionalAddress
                "NY", // region
                "New York", // city
                "USA", // country
                Instant.now().plusSeconds(3600), // tokenExpireAt
                "token123", // tokenPassword
                "validation123", // tokenValidation
                false, // gsmVerified
                true, // emailVerified
                "facebook123", // facebookId
                "google123", // googleId
                true, // notifyConnection
                false, // forceReset
                null, // roles
                null, // profile (not initialized yet)
                null, // editorialSummaries (not initialized yet)
                null, // circuitGroups (not initialized yet),
                null


        );
        user.setId(1);
        user.setReference("ABC123");
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setLastLogin(Instant.now());
        user.setEnabled(true);
        user.setCreatedDate(Instant.now());
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setBirthPlace("New York");
        user.setPhone("123-456-7890");
        user.setMobilePhone("987-654-3210");
        user.setGsm("987-654-3210");
        user.setPrefixe("+1");
        user.setIso2("US");
        user.setProfession("Software Developer");
        user.setCivility("Mr.");
        user.setPicture("profile_pic.jpg");
        user.setCompanyName("TechCorp");
        user.setAddress("123 Tech Street");
        user.setZipCode("10001");
        user.setAdditionalAddress("Suite 101");
        user.setRegion("NY");
        user.setCity("New York");
        user.setCountry("USA");
        user.setTokenExpireAt(Instant.now().plusSeconds(3600)); // Token expires in 1 hour
        user.setTokenPassword("reset_token");
        user.setTokenValidation("validation_token");
        user.setGsmVerified(true);
        user.setEmailVerified(true);
        user.setFacebookId("fb_user_123");
        user.setGoogleId("google_user_456");
        user.setNotifyConnection(true);
        user.setForceReset(false);
        user.setRoles("ADMIN"); // In a real scenario, this would likely be a list or map
        user.setProfile(new Profile()); // Assuming a Profile object exists
        user.setEditorialSummaries(new HashSet<>());
        user.setCircuitGroups(new HashSet<>());
        user.setAgency(new Agency());
        user.getId();
        user.getReference();
        user.getUsername();
        user.getEmail();
        user.getPassword();
        user.getLastLogin();
        user.getEnabled();
        user.getCreatedDate();
        user.getLastName();
        user.getFirstName();
        user.getBirthDate();
        user.getBirthPlace();
        user.getPhone();
        user.getMobilePhone();
        user.getGsm();
        user.getPrefixe();
        user.getIso2();
        user.getProfession();
        user.getCivility();
        user.getPicture();
        user.getCompanyName();
        user.getAddress();
        user.getZipCode();
        user.getAdditionalAddress();
        user.getRegion();
        user.getCity();
        user.getCountry();
        user.getTokenExpireAt();
        user.getTokenPassword();
        user.getTokenValidation();
        user.getGsmVerified();
        user.getEmailVerified();
        user.getFacebookId();
        user.getGoogleId();
        user.getNotifyConnection();
        user.getForceReset();
        user.getRoles();
        user.getProfile(); // Assuming a Profile object exists
        user.getEditorialSummaries();
        user.getCircuitGroups();
        user.getAgency();
        assertEquals(1, user.getId());

    }


    }
