package com.ouitrips.app.services.places.impl;

import com.ouitrips.app.entities.places.*;
import com.ouitrips.app.entities.places.PlaceType;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.googlemapsservice.model.*;
import com.ouitrips.app.mapper.security.places.PlaceMapper;
import com.ouitrips.app.osrmservice.Table;
import com.ouitrips.app.repositories.security.places.*;
import com.ouitrips.app.services.places.IPlaceService;
import com.ouitrips.app.services.places.TSPService;
import com.ouitrips.app.utils.DateUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalTime;

@Service
@AllArgsConstructor
public class PlaceServiceImpl implements IPlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final PlaceInfoRepository placeInfoRepository;
    private final PlaceDurationRepository placeDurationRepository;
    private final PlacePricingRepository placePricingRepository;
    private final PlaceOpeningHourRepository placeOpeningHourRepository;
    private final PlaceTimeZoneRepository placeTimeZoneRepository;
    private final CityRepository cityRepository;
    private final LinksPtRepository linksPtRepository;
    private final PlaceTypeRepository placeTypeRepository;
    private final LinksTpRepository linksTpRepository;
    private final PlaceTagRepository placeTagRepository;
    private final Table tableService;
    private final TSPService tspService;
    private final CitySummaryRepository citySummaryRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceServiceImpl.class);

    @Override
    @Transactional
    public Object save(Map<String, Object> params) {
        String title = (String) params.get("title");
        String description = (String) params.get("description");
        String content = (String) params.get("content");
        Double latitude = (Double) params.get("latitude");
        Double longitude = (Double) params.get("longitude");
        String address = (String) params.get("address");
        String popularity = (String) params.get("popularity");
        String timeZoneName = (String) params.get("timeZoneName");
        Integer cityReference = (Integer) params.get("cityReference");
        String placeTypeCode = (String) params.get("placeTypeCode");
        String tag = (String) params.get("placeTag");
        Integer id = (Integer) params.get("id");
        Place place;
        PlaceInfo placeInfo;
        PlaceDuration placeDuration;
        PlacePricing placePricing;
        PlaceOpeningHour placeOpeningHour;
        if (id == null) {
            if (title == null || latitude == null || longitude == null) {
                throw new ExceptionControllerAdvice.GeneralException("Title, latitude, and longitude are required for creating a new place.");
            }
            place = new Place();
            placeInfo = new PlaceInfo();
            placeDuration = new PlaceDuration();
            placePricing = new PlacePricing();
            placeOpeningHour = new PlaceOpeningHour();
        } else {
            place = placeRepository.findById(id).orElse(new Place());
            //Todo check if we should update just one placeInfo or update by place info id
            if(!place.getPlaceInfos().isEmpty())
                placeInfo = place.getPlaceInfos().iterator().next();
            else
                placeInfo = new PlaceInfo();
            // Handle PlaceDuration
            if(!place.getPlaceDurations().isEmpty())
                placeDuration = place.getPlaceDurations().iterator().next();
            else
                placeDuration = new PlaceDuration();
            // Handle PlacePricing
            if (!place.getPlacePricing().isEmpty())
                placePricing = place.getPlacePricing().iterator().next();
            else
                placePricing = new PlacePricing();
            // Handle PlaceOpeningHour
            if (!place.getPlaceOpeningHours().isEmpty())
                placeOpeningHour = place.getPlaceOpeningHours().iterator().next();
            else
                placeOpeningHour = new PlaceOpeningHour();
        }
        //Place Param
        if (title != null) place.setTitle(title);
        if (description != null) place.setDescription(description);
        if (content != null) place.setContent(content);
        if (latitude != null) place.setLatitude(latitude);
        if (longitude != null) place.setLongitude(longitude);
        if (address != null) place.setAddress(address);
        if (popularity != null) place.setPopularity(popularity);
        if (timeZoneName != null){
            PlaceTimeZone placeTimeZone = placeTimeZoneRepository.findByName(timeZoneName);
            if(placeTimeZone != null)
                place.setPlaceTimeZone(placeTimeZone);
        }
        //Todo City
        if (cityReference != null) {
            City city = cityRepository.findById(cityReference).orElse(null);
            if (city != null) {
                place.setCity(city);
            }
        }
        //Save Place
//        placeRepository.save(place);
        //Place type
        if(placeTypeCode != null){
            PlaceType placeType = placeTypeRepository.findByCode(placeTypeCode);
            if(placeType != null){
                LinksPtId linksPtId = new LinksPtId(place.getId(), placeType.getId());
                if (!linksPtRepository.existsById(linksPtId)) {//Not exists
                    //Todo update LinkPt for this place with the new PlaceType
                    LinksPt linksPt = linksPtRepository.findByPlace(place);
                    if(linksPt == null){
                        linksPt = new LinksPt();
                    }
                    linksPt.setId(linksPtId);
                    linksPt.setPlace(place);
                    linksPt.setPlaceType(placeType);
                    linksPtRepository.save(linksPt);
                }
            }
        }
        //Place tag
        if (tag != null) {
            PlaceTag placeTag = placeTagRepository.findByTag(tag);
            if (placeTag != null) {
                LinksTpId linksTpId = new LinksTpId(place.getId(), placeTag.getId());
                if (!linksTpRepository.existsById(linksTpId)) {
                    LinksTp linksTp = linksTpRepository.findByPlaceAndPlaceTag(place, placeTag);
                    if(linksTp == null){
                        linksTp = new LinksTp();
                    }
                    linksTp.setId(linksTpId);
                    linksTp.setPlace(place);
                    linksTp.setPlaceTag(placeTag);
                    linksTpRepository.save(linksTp);
                }
            }
        }
        //Place info param
        String addressPlaceInfo = (String) params.get("addressPlaceInfo");
        String phone = (String) params.get("phone");
        String phone2 = (String) params.get("phone2");
        String website = (String) params.get("website");
        String contentPlaceInfo = (String) params.get("contentPlaceInfo");
        if(contentPlaceInfo != null) placeInfo.setContent(contentPlaceInfo);
        if(addressPlaceInfo != null) placeInfo.setAddress(addressPlaceInfo);
        if(phone != null) placeInfo.setPhone(phone);
        if(phone2 != null) placeInfo.setPhone2(phone2);
        if(website != null) placeInfo.setWebsite(website);
        placeInfo.setPlace(place);
        //PlaceDuration params
//        LocalTime duration = (LocalTime) params.get("duration");
        Long duration = (Long) params.get("duration");
        if (duration != null) {
            placeDuration.setDuration(duration);
            placeDuration.setPlace(place);
        }
        // PlacePricing params
        Double priceMin = (Double) params.get("price_min");
        Double priceMax = (Double) params.get("price_max");
        Double price = (Double) params.get("price");
        if (priceMin != null) {
            placePricing.setPriceMin(priceMin);
        }
        if (priceMax != null) {
            placePricing.setPriceMax(priceMax);
        }
        if (price != null) {
            placePricing.setPrice(price);
        }
        placePricing.setPlace(place);
        // PlaceOpeningHour params
        LocalTime openingTime = (LocalTime) params.get("opening_time");
        LocalTime closeTime = (LocalTime) params.get("close_time");
        if (openingTime != null) {
            placeOpeningHour.setOpeningTime(openingTime);
        }
        if (closeTime != null) {
            placeOpeningHour.setCloseTime(closeTime);
        }
        placeOpeningHour.setPlace(place);
        //Save all place detail
        if(id != null){//case: Update
                placeInfoRepository.save(placeInfo);
                placeDurationRepository.save(placeDuration);
                placePricingRepository.save(placePricing);
                placeOpeningHourRepository.save(placeOpeningHour);
        }else{//Case Create
            if(contentPlaceInfo != null){
                placeInfoRepository.save(placeInfo);
            }
            if(duration != null)
                placeDurationRepository.save(placeDuration);
            if (price != null) {
                placePricingRepository.save(placePricing);
            }
            if (openingTime != null || closeTime != null) {
                placeOpeningHourRepository.save(placeOpeningHour);
            }
        }
        return Map.of("reference", placeRepository.save(place).getId());
    }
    @Override
    public void delete(Integer id) {
        placeRepository.delete(this.getById(id));
    }
    @Override
    @Transactional
    public Object get(Integer id) {
        return placeMapper.apply(this.getById(id));
    }
    @Override
    @Transactional
    public Object getAll() {
        return placeRepository.findAll().stream().map(placeMapper).toList();
    }
    public Place getById(Integer id) {
        return placeRepository.findById(id).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("Place not found")
        );
    }
//    @Override
//    @Transactional
//    public Object searchPlace(Map<String, Object> params){
//        System.out.println("Params: " + params);
//        List<String> citiesSource = (List<String>) params.get("sourceCities");
//        List<String> citiesVisited = (List<String>) params.get("visitedCities");
//        List<String> cityDestination = (List<String>) params.get("destinationCities");
//        List<LatLng> latLngs = new ArrayList<>();
//        Map<Integer, Place> placeIndexToName = new HashMap<>();
//        int i = 0;
//        City citySource = null;
//        List<Place> placesSource = new ArrayList<>();
//        // Fetching the date strings
//        String outboundDepartureStart = (String) params.get("outboundDepartureDateStart");
//        String inboundDepartureStart = (String) params.get("inboundDepartureDateStart");
//        // Parsing the date strings to Date objects
//        Date outboundDepartureDate = null;
//        Date inboundDepartureDate = null;
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            if (outboundDepartureStart != null) {
//                outboundDepartureDate = dateFormat.parse(outboundDepartureStart);
//            }
//            if (inboundDepartureStart != null) {
//                inboundDepartureDate = dateFormat.parse(inboundDepartureStart);
//            }
//        } catch (ParseException e) {
//            LOGGER.error("Error parsing dates: outboundDepartureStart={}, inboundDepartureStart={}", outboundDepartureStart, inboundDepartureStart, e);
//        }
//        if (outboundDepartureDate != null && inboundDepartureDate != null) {
//            if (outboundDepartureDate.after(inboundDepartureDate)) {
//                throw new ExceptionControllerAdvice.GeneralException("Outbound departure date cannot be after inbound departure date.");
//            }
//            DateUtil dateUtil = new DateUtil();
//            long daysBetween = dateUtil.daysBetween(outboundDepartureDate, inboundDepartureDate);
//            System.out.println("Number of days: " + daysBetween);
//        } else {
//            System.out.println("One or both dates are null.");
//        }
//
//        if (citiesSource != null) {
//            for (String city : citiesSource) {
//                //Todo a voir recherche par un column unique (par exemple: reference, code, id);
//                citySource = cityRepository.findByName(city);
//                if (citySource != null) {
//                    Page<Place> placesForCity = placeRepository.search(citySource.getId(), PageRequest.of(0, 3));
//                    placesSource.addAll(placesForCity.getContent());
//                }else {
//                    System.out.println("CitySource not found:"+ city);
//                }
//            }
//        }
//        List<Place> placesVisited = new ArrayList<>();
//        if (citiesVisited != null) {
//            for (String city : citiesVisited) {
//                City cityEntity = cityRepository.findByName(city);
//                if (cityEntity != null) {
//                    Page<Place> placesForCity = placeRepository.search(cityEntity.getId(), PageRequest.of(0, 10));
//                    placesVisited.addAll(placesForCity.getContent());
//                }else {
//                    System.out.println("CityVisited not found:"+ city);
//                }
//            }
//        }
//        City destinationCity = null;
//        List<Place> placesDestination = new ArrayList<>();
//        if (cityDestination != null) {
//            for (String city : cityDestination) {
//                destinationCity = cityRepository.findByName(city);
//                if (destinationCity != null) {
//                    Page<Place> placesForCity = placeRepository.search(destinationCity.getId(), PageRequest.of(0, 3));
//                    placesDestination.addAll(placesForCity.getContent());
//                }else {
//                    System.out.println("CityDestination not found:"+ city);
//                }
//            }
//        }
//        // Prepare coordinates string for OSRM
//        StringBuilder coordinates = new StringBuilder();
//        //TODO:city
//        latLngs.add(new LatLng(citySource.getLatitude(), citySource.getLongitude()));
//        Place citySourceAsPlace = new Place();
//        citySourceAsPlace.setTitle(citySource.getName());
//        citySourceAsPlace.setLatitude(citySource.getLatitude());
//        citySourceAsPlace.setLongitude(citySource.getLongitude());
//        placeIndexToName.put(i++, citySourceAsPlace);
//        coordinates.append(citySource.getLongitude()).append(",").append(citySource.getLatitude()).append(";");
//        long totalDurationVisited = 0;
//        for (Place place : placesVisited) {
//            latLngs.add(new LatLng(place.getLatitude(), place.getLongitude()));
//            placeIndexToName.put(i++, place);
//            coordinates.append(place.getLongitude()).append(",").append(place.getLatitude()).append(";");
//            //Tood: get place_duration by place, if not exists set to 0, if exists==> calculate the duration
//            Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(place);
//            long placeDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
//            totalDurationVisited += placeDuration;
//        }
//
//        latLngs.add(new LatLng(destinationCity.getLatitude(), destinationCity.getLongitude()));
//        Place destinationCityAsPlace = new Place();
//        destinationCityAsPlace.setTitle(destinationCity.getName());
//        destinationCityAsPlace.setLatitude(destinationCity.getLatitude());
//        destinationCityAsPlace.setLongitude(destinationCity.getLongitude());
//        placeIndexToName.put(i++, destinationCityAsPlace);
//        coordinates.append(destinationCity.getLongitude()).append(",").append(destinationCity.getLatitude()).append(";");
//
//        // Remove the last semicolon
//        if (coordinates.length() > 0) {
//            coordinates.setLength(coordinates.length() - 1);
//        }
//        JSONObject distanceMatrixResponse = tableService.generateTravelTimeMatrix(coordinates.toString(), "car", "distance,duration");
//        List<List<Double>> distanceMatrix = extractDistanceMatrix(distanceMatrixResponse);
//        List<List<Double>> durationMatrix = extractDurationMatrix(distanceMatrixResponse);
//        List<Integer> optimalRouteIndices = tspService.findMinDistanceRoute(distanceMatrix);
//        List<Place> orderedPlaces = new ArrayList<>();
//        double totalDuration = 0.0;
//        Integer previousIndex = null;
//        for (Integer currentIndex : optimalRouteIndices) {
//            if (previousIndex != null) {
//                totalDuration += durationMatrix.get(previousIndex).get(currentIndex);
//            }
//            orderedPlaces.add(placeIndexToName.get(currentIndex));
//            previousIndex = currentIndex;
//        }
//
//        int hours = (int) (totalDuration / 3600);
//        int minutes = (int) ((totalDuration % 3600) / 60);
//
//        String formattedDuration = String.format("%d hours %d minutes", hours, minutes);
//        System.out.println("Total Duration: " + formattedDuration);
//
//        int totalHoursVisited = (int) (totalDurationVisited / 3600);
//        int totalMinutesVisited = (int) ((totalDurationVisited % 3600) / 60);
//
//        String formattedTotalDurationVisited = String.format("%d hours %d minutes", totalHoursVisited, totalMinutesVisited);
//        System.out.println("Total Duration for Visited Places: " + formattedTotalDurationVisited);
//        Map<String, Object> response = new HashMap<>();
////        response.put("placesSource", placesSource.stream().map(placeMapper).toList());
////        response.put("placesVisited", placesVisited.stream().map(placeMapper).toList());
////        response.put("placesDestination", placesDestination.stream().map(placeMapper).toList());
//        response.put("OptimalRoute", orderedPlaces.stream().map(placeMapper).toList());
//        return response;
//    }
//    @Override
//    @Transactional
//    public Object searchPlaceTest(Map<String, Object> params) {
//        List<String> citiesSource = (List<String>) params.get("sourceCities");
//        List<String> citiesVisited = (List<String>) params.get("visitedCities");
//        List<String> cityDestination = (List<String>) params.get("destinationCities");
//        List<LatLng> latLngs = new ArrayList<>();
//        Map<Integer, Place> placeIndexToName = new HashMap<>();
//        int i = 0;
//        City citySource = null;
//        List<Place> placesSource = new ArrayList<>();
//        // Fetching the date strings
//        String outboundDepartureStart = (String) params.get("outboundDepartureDateStart");
//        String inboundDepartureStart = (String) params.get("inboundDepartureDateStart");
//        // Parsing the date strings to Date objects
//        Date outboundDepartureDate = null;
//        Date inboundDepartureDate = null;
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            if (outboundDepartureStart != null) {
//                outboundDepartureDate = dateFormat.parse(outboundDepartureStart);
//            }
//            if (inboundDepartureStart != null) {
//                inboundDepartureDate = dateFormat.parse(inboundDepartureStart);
//            }
//        } catch (ParseException e) {
//            LOGGER.error("Error parsing dates: outboundDepartureStart={}, inboundDepartureStart={}", outboundDepartureStart, inboundDepartureStart, e);
//        }
//        long daysBetween = 0;
//        if (outboundDepartureDate != null && inboundDepartureDate != null) {
//            if (outboundDepartureDate.after(inboundDepartureDate)) {
//                throw new ExceptionControllerAdvice.GeneralException("Outbound departure date cannot be after inbound departure date.");
//            }
//            DateUtil dateUtil = new DateUtil();
//            daysBetween = dateUtil.daysBetween(outboundDepartureDate, inboundDepartureDate);
//            System.out.println("Number of days: " + daysBetween);
//        } else {
//            System.out.println("One or both dates are null.");
//        }
//
//        // Initialize maxDaysPerCity dynamically using city summaries
//
//        //Var map:  (city_id, totalDuration)
//
//        //Call The method Probability
//        ///City1 2j,  City2 3j  ==> Probability
//        int[] maxDaysPerCity = new int[citiesVisited.size()];
//        for (int n = 0; n < citiesVisited.size(); n++) {
//            City city = cityRepository.findByName(citiesVisited.get(i));
//            if (city != null) {
//                Optional<CitySummary> citySummaryOpt = citySummaryRepository.findByCity(city);
//                if (citySummaryOpt.isPresent()) {
//                    CitySummary citySummary = citySummaryOpt.get();
//                    Long totalDuration = citySummary.getTotalDuration();
//                    if (totalDuration != null) {
//                        maxDaysPerCity[n] = Math.toIntExact(totalDuration); // Convert Long to int
//                    } else {
//                        throw new IllegalStateException("Total duration is null for city: " + city.getName());
//                    }
//                } else {
//                    throw new IllegalStateException("City summary not found for city: " + city.getName());
//                }
//            } else {
//                throw new IllegalStateException("City not found: " + citiesVisited.get(n));
//            }
//        }
//        // Initialize minDaysPerCity to 1 day per city
//        List<Integer> minDaysPerCity = new ArrayList<>();
//        for (int j = 0; j < citiesVisited.size(); j++) {
//            minDaysPerCity.add(1);
//        }
//
//        int remainingDays = (int) (daysBetween - citiesVisited.size());  // Remaining days to distribute
//
//        // Call the dynamic generatePlans method to create travel plans
//
//        List<Map<String, Integer>> allPlans = new ArrayList<>();
//        generatePlans(remainingDays, citiesVisited.toArray(new String[0]), minDaysPerCity, maxDaysPerCity, allPlans);
//
//        //City Summary totalDuration   Tanger   20j
//        // Print or return the generated plans (for now we print)
//        System.out.println("All possible plans:");
//        List<List<Place>> allPlaces = new ArrayList<>();
//        long totalDurerVisited = 0;
//        final long MAX_DURATION = 3 * 3600; // 12 hours in seconds
//        int maxPlans = 4;
//        for (Map<String, Integer> plan : allPlans) {//If total plans is 10; Each plan contain: "Tanger": 3, "Tetouan": 2, "Larache": 1 & "Tanger": 2, "Tetouan": 3, "Larache": 1
//            //Each plan ==> Circuit --> list<Place>
//            System.out.println("##### Plan #####");
//            List<Place> placesVisited = new ArrayList<>();
//            for (String city: plan.keySet()){
//                City cityEntity = cityRepository.findByName(city);
//                if (cityEntity != null) {
//                    int pageNumber = 0;
//                    boolean keepFetching = true;
//                    while (keepFetching) {
//                        Page<Place> placesForCity = placeRepository.search(cityEntity.getId(), PageRequest.of(pageNumber++, 3));
//                        List<Place> currentPagePlaces = placesForCity.getContent();
//
//                        if (currentPagePlaces.isEmpty()) {
//                            keepFetching = false;
//                        } else {
//                            for (Place place : currentPagePlaces) {
//                                Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(place);
//                                long placeDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
//
//                                if (totalDurerVisited + placeDuration <= MAX_DURATION) {
//                                    placesVisited.add(place);
//                                    totalDurerVisited += placeDuration;
//                                } else {
//                                    keepFetching = false;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    System.out.println("CityVisited not found:" + city);
//                }
//            }
//            allPlaces.add(placesVisited);
//            maxPlans--;
//            if(maxPlans == 0){
//                break;
//            }
//        }
//        //Max 4 List<Places>
//        //foreach allPlaces ==> Use algotithm ORSM ==> TSP ==> Circuit
//
//
//        if (citiesSource != null) {
//            for (String city : citiesSource) {
//                citySource = cityRepository.findByName(city);
//                if (citySource != null) {
//                    Page<Place> placesForCity = placeRepository.search(citySource.getId(), PageRequest.of(0, 3));
//                    placesSource.addAll(placesForCity.getContent());
//                } else {
//                    System.out.println("CitySource not found:" + city);
//
//                }
//            }
//        }
//
//        /*List<Place> placesVisited = new ArrayList<>();
//        long totalDurerVisited = 0;
//        final long MAX_DURATION = 12 * 3600; // 12 hours in seconds
//        if (citiesVisited != null) {
//            for (String city : citiesVisited) {
//                City cityEntity = cityRepository.findByName(city);
//                if (cityEntity != null) {
//                    int pageNumber = 0;
//                    boolean keepFetching = true;
//
//                    while (keepFetching) {
//                        Page<Place> placesForCity = placeRepository.search(cityEntity.getId(), PageRequest.of(pageNumber++, 3));
//                        List<Place> currentPagePlaces = placesForCity.getContent();
//
//                        if (currentPagePlaces.isEmpty()) {
//                            keepFetching = false;
//                        } else {
//                            for (Place place : currentPagePlaces) {
//                                Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(place);
//                                long placeDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
//
//                                if (totalDurerVisited + placeDuration <= MAX_DURATION) {
//                                    placesVisited.add(place);
//                                    totalDurerVisited += placeDuration;
//                                } else {
//                                    keepFetching = false;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    System.out.println("CityVisited not found:" + city);
//                }
//            }
//        }*/
//        City destinationCity = null;
//        List<Place> placesDestination = new ArrayList<>();
//        if (cityDestination != null) {
//            for (String city : cityDestination) {
//                destinationCity = cityRepository.findByName(city);
//                if (destinationCity != null) {
//                    Page<Place> placesForCity = placeRepository.search(destinationCity.getId(), PageRequest.of(0, 3));
//                    placesDestination.addAll(placesForCity.getContent());
//                } else {
//                    System.out.println("cityDestination not found:" + city);
//
//                }
//            }
//        }
//        // Prepare coordinates string for OSRM
//        StringBuilder coordinates = new StringBuilder();

//        latLngs.add(new LatLng(citySource.getLatitude(), citySource.getLongitude()));
//        Place citySourceAsPlace = new Place();
//        citySourceAsPlace.setTitle(citySource.getName());
//        citySourceAsPlace.setLatitude(citySource.getLatitude());
//        citySourceAsPlace.setLongitude(citySource.getLongitude());
//        placeRepository.save(citySourceAsPlace);
//        placeIndexToName.put(i++, citySourceAsPlace);
//        coordinates.append(citySource.getLongitude()).append(",").append(citySource.getLatitude()).append(";");
//        long totalDurationVisited = 0;
////        for (Place place : placesVisited) {
////            latLngs.add(new LatLng(place.getLatitude(), place.getLongitude()));
////            placeIndexToName.put(i++, place);
////            coordinates.append(place.getLongitude()).append(",").append(place.getLatitude()).append(";");
////            Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(place);
////            long placeDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
////            totalDurationVisited += placeDuration;
////        }
//        latLngs.add(new LatLng(destinationCity.getLatitude(), destinationCity.getLongitude()));
//        Place destinationCityAsPlace = new Place();
//        destinationCityAsPlace.setTitle(destinationCity.getName());
//        destinationCityAsPlace.setLatitude(destinationCity.getLatitude());
//        destinationCityAsPlace.setLongitude(destinationCity.getLongitude());
//        placeRepository.save(destinationCityAsPlace);
//        placeIndexToName.put(i++, destinationCityAsPlace);
//        coordinates.append(destinationCity.getLongitude()).append(",").append(destinationCity.getLatitude()).append(";");
//        // Remove the last semicolon
//        if (coordinates.length() > 0) {
//            coordinates.setLength(coordinates.length() - 1);
//        }
//        JSONObject distanceMatrixResponse = tableService.generateTravelTimeMatrix(coordinates.toString(), "car", "distance,duration");
//        List<List<Double>> distanceMatrix = extractDistanceMatrix(distanceMatrixResponse);
//        List<List<Double>> durationMatrix = extractDurationMatrix(distanceMatrixResponse);
//        List<Integer> optimalRouteIndices = tspService.findMinDistanceRoute(distanceMatrix);
//        List<Place> orderedPlaces = new ArrayList<>();
//        double totalDuration = 0.0;
//        Integer previousIndex = null;
//        for (Integer currentIndex : optimalRouteIndices) {
//            if (previousIndex != null) {
//                totalDuration += durationMatrix.get(previousIndex).get(currentIndex);
//            }
//            orderedPlaces.add(placeIndexToName.get(currentIndex));
//            previousIndex = currentIndex;
//        }
//        int hours = (int) (totalDuration / 3600);
//        int minutes = (int) ((totalDuration % 3600) / 60);
//        String formattedDuration = String.format("%d hours %d minutes", hours, minutes);
//        System.out.println("Total Duration: " + formattedDuration);
//        // Update city summaries based on visited cities
//        updateCitySummaries(citiesVisited);
//
//        // Fetch and log city summary information
//        for (String cityName : citiesVisited) {
//            City city = cityRepository.findByName(cityName);
//            if (city != null) {
//                Optional<CitySummary> citySummaryOpt = citySummaryRepository.findByCity(city);
//                if (citySummaryOpt.isPresent()) {
//                    CitySummary citySummary = citySummaryOpt.get();
//                    System.out.println("City: " + city.getName() + ", Total Places: " + citySummary.getTotalPlaces() + ", Total Duration: " + citySummary.getTotalDuration());
//                }
//            }
//        }
//        int totalHoursVisited = (int) (totalDurationVisited / 3600);
//        int totalMinutesVisited = (int) ((totalDurationVisited % 3600) / 60);
//        String formattedTotalDurationVisited = String.format("%d hours %d minutes", totalHoursVisited, totalMinutesVisited);
//        // Initialize a map
//        Map<Integer, Double> placeTrajectDurations = new HashMap<>();
//        Map<Integer, Double> placeTrajectDistance = new HashMap<>();
//        Map<Integer, List<Place>> dailyItineraryMap = new HashMap<>();
//        List<Place> currentDayPlaces = new ArrayList<>();
//        double currentDayDuration = 0.0;  // in seconds
//        int dayCount = 1;
//        long totalVisitedDuration = 0;
//        Integer previousPlaceIndex = null;
//        for (Integer currentIndex : optimalRouteIndices) {
//            Place currentPlace = placeIndexToName.get(currentIndex);
//            // Calculate travel time from the previous place
//            double travelDuration = 0.0;
//            if (previousPlaceIndex != null) {
//                travelDuration = durationMatrix.get(previousPlaceIndex).get(currentIndex);
//            }
//            placeTrajectDurations.put(currentPlace.getId(), travelDuration);
//            // Calculate travel distance from the previous place
//            double travelDistance = 0.0;
//            if (previousPlaceIndex != null) {
//                travelDistance = distanceMatrix.get(previousPlaceIndex).get(currentIndex);
//                // Convert meters to kilometers
//                travelDistance /= 1000;
//                // Format to 2 decimal places (optional)
//                travelDistance = Math.round(travelDistance * 100.0) / 100.0;
//            }
//            placeTrajectDistance.put(currentPlace.getId(), travelDistance);
//            // Calculate visit duration at the current place
//            Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(currentPlace);
//            long visitDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
//
//            if (currentDayDuration + travelDuration + visitDuration <= 3 * 3600) {
//                // Add to the current day's itinerary
//                currentDayPlaces.add(currentPlace);
//                currentDayDuration += travelDuration + visitDuration;
//                totalVisitedDuration += visitDuration;
//            } else {
//                dailyItineraryMap.put(dayCount, new ArrayList<>(currentDayPlaces));
//                dayCount++;
//                currentDayPlaces.clear();
//                currentDayDuration = travelDuration + visitDuration;
//                currentDayPlaces.add(currentPlace);
//                totalVisitedDuration += visitDuration;
//            }
//
//            previousPlaceIndex = currentIndex;  // Update the previous index for the next iteration
//        }
//        // After the loop, store the last day's places
//        if (!currentDayPlaces.isEmpty()) {
//            dailyItineraryMap.put(dayCount, new ArrayList<>(currentDayPlaces));
//        }
//
//        // Formatting total travel and visit durations
//        int visitedHoursTotal = (int) (totalVisitedDuration / 3600);
//        int visitedMinutesTotal = (int) ((totalVisitedDuration % 3600) / 60);
//        String formattedVisitedDurationTotal = String.format("%d hours %d minutes", visitedHoursTotal, visitedMinutesTotal);
//        System.out.println("Total Duration for Visited Places: " + formattedVisitedDurationTotal);
//
//        Map<Integer, List<Object>> mappedDailyItinerary = new HashMap<>();
//        for (Map.Entry<Integer, List<Place>> entry : dailyItineraryMap.entrySet()) {
//            List<Object> mappedPlaces = entry.getValue().stream()
//                    .map(e -> placeMapper.apply2(
//                                    e,
//                                    placeTrajectDurations.get(e.getId()),
//                                    placeTrajectDistance.get(e.getId())
//                            )
//                    )
//                    .toList();
//            mappedDailyItinerary.put(entry.getKey(), mappedPlaces);
//        }
//        Map<String, Object> response = new HashMap<>();
//        response.put("DailyItinerary", mappedDailyItinerary);
//        response.put("TotalDurationVisited", formattedTotalDurationVisited);
//        //placesSource===> (place4, place5)
////        response.put("placesDestination", placesDestination.stream().map(placeMapper).toList());
//        return response;
//    }
@Override
@Transactional
public Object searchTestPlace(Map<String, Object> params) {
    List<String> citiesSource = (List<String>) params.get("sourceCities");
    List<String> citiesVisited = (List<String>) params.get("visitedCities");
    List<String> cityDestination = (List<String>) params.get("destinationCities");
    List<LatLng> latLngs = new ArrayList<>();
    List<String> excludedPlaceType = (List<String>) params.get("excludedPlaceType");
    List<String> includePlaceType = (List<String>) params.get("includePlaceType");
    Map<Integer, Place> placeIndexToName = new HashMap<>();
    int i = 0;
    City citySource = null;
    List<Place> placesSource = new ArrayList<>();
    // Fetching the date strings
    String outboundDepartureStart = (String) params.get("outboundDepartureDateStart");
    String inboundDepartureStart = (String) params.get("inboundDepartureDateStart");
    // Parsing the date strings to Date objects
    Date outboundDepartureDate = null;
    Date inboundDepartureDate = null;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
        if (outboundDepartureStart != null) {
            outboundDepartureDate = dateFormat.parse(outboundDepartureStart);
        }
        if (inboundDepartureStart != null) {
            inboundDepartureDate = dateFormat.parse(inboundDepartureStart);
        }
    } catch (ParseException e) {
        LOGGER.error("Error parsing dates: outboundDepartureStart={}, inboundDepartureStart={}", outboundDepartureStart, inboundDepartureStart, e);
    }
    long daysBetween = 0;
    if (outboundDepartureDate != null && inboundDepartureDate != null) {
        if (outboundDepartureDate.after(inboundDepartureDate)) {
            throw new ExceptionControllerAdvice.GeneralException("Outbound departure date cannot be after inbound departure date.");
        }
        DateUtil dateUtil = new DateUtil();
        daysBetween = dateUtil.daysBetween(outboundDepartureDate, inboundDepartureDate);
        System.out.println("Number of days: " + daysBetween);
    } else {
        System.out.println("One or both dates are null.");
    }

    updateCitySummaries(citiesVisited);
    int[] maxDaysPerCity = new int[citiesVisited.size()];
    for (int n = 0; n < citiesVisited.size(); n++) {
        City city = cityRepository.findByName(citiesVisited.get(n));
        if (city != null) {
            System.out.println("Found city: " + city.getName());
            Optional<CitySummary> citySummaryOpt = citySummaryRepository.findByCity(city);
            if (citySummaryOpt.isPresent()) {
                CitySummary citySummary = citySummaryOpt.get();
                Long totalDurationInDays = citySummary.getTotalDuration();  // Total duration is now in days
                System.out.println("Total duration for " + citySummary.getCity().getName() + ": " + totalDurationInDays);
                if (totalDurationInDays != null && totalDurationInDays > 0) {
                    maxDaysPerCity[n] = Math.toIntExact(totalDurationInDays); // Convert Long to int
                } else {
                    throw new ExceptionControllerAdvice.GeneralException("Total duration is null or zero for city: " + city.getName());
                }
            } else {
                throw new ExceptionControllerAdvice.GeneralException("City summary not found for city: " + city.getName());
            }
        } else {
            throw new ExceptionControllerAdvice.GeneralException("City not found: " + citiesVisited.get(n));
        }
    }

    // Initialize minDaysPerCity to 1 day per city
    List<Integer> minDaysPerCity = new ArrayList<>();
    for (int j = 0; j < citiesVisited.size(); j++) {
        minDaysPerCity.add(1);
    }
    int remainingDays = (int) (daysBetween - citiesVisited.size());
    List<Map<String, Integer>> allPlans = new ArrayList<>();
    allPlans = generatePlans(remainingDays, citiesVisited.toArray(new String[0]), minDaysPerCity, maxDaysPerCity, allPlans);
    List<List<Place>> allPlaces = new ArrayList<>();
    int maxPlans = ProbabilityTable.getProbabilityCircuits(citiesVisited.size(), (int) daysBetween);
    System.out.println("Max Plans: " + maxPlans);
    for (Map<String, Integer> plan : allPlans) {
        if (maxPlans == 0) {
            break;
        }
        List<Place> placesVisited = new ArrayList<>();
        // After generating plans
        System.out.println("Generated Plans: " + allPlans);
        for (String city: plan.keySet()){
            long totalDurerVisited = 0;
            final long MAX_DURATION = plan.get(city) * (long) 3 * 3600;
            City cityEntity = cityRepository.findByName(city);
            if (cityEntity != null) {
                int pageNumber = 0;
                boolean keepFetching = true;
                while (keepFetching) {
                    Page<Place> placesForCity = placeRepository.search(cityEntity.getId(), excludedPlaceType, includePlaceType, PageRequest.of(pageNumber++, 3));
                    System.out.println("Places for City: " + placesForCity.getContent());
                    List<Place> currentPagePlaces = placesForCity.getContent();
                    if (currentPagePlaces.isEmpty()) {
                        keepFetching = false;
                    } else {
                        System.out.println("Fetched places for " + city + ": " + currentPagePlaces);
                        for (Place place : currentPagePlaces) {
                            Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(place);
                            long placeDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
                            if (totalDurerVisited + placeDuration <= MAX_DURATION) {
                                placesVisited.add(place);
                                totalDurerVisited += placeDuration;
                            } else {
                                keepFetching = false;
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("CityVisited not found:" + city);
            }
        }
        allPlaces.add(new ArrayList<>(placesVisited));
        placesVisited.clear();
        maxPlans--;
    }
    List<Map<String, Object>> allCircuits = new ArrayList<>();
    for (List<Place> placesVisited : allPlaces) {
        StringBuilder coordinates = new StringBuilder();
        long totalDurationVisited = 0;
        double totalDistance = 0.0;
        Map<String, Long> placesCountByCity = new HashMap<>();
        for (Place place : placesVisited) {
            latLngs.add(new LatLng(place.getLatitude(), place.getLongitude()));
            placeIndexToName.put(i++, place);
            coordinates.append(place.getLongitude()).append(",").append(place.getLatitude()).append(";");

            City city = place.getCity();
            if (city != null) {
                String cityName = city.getName();
                placesCountByCity.put(cityName, placesCountByCity.getOrDefault(cityName, 0L) + 1);
            }
            Optional<PlaceDuration> placeDurationOpt = placeDurationRepository.findByPlace(place);
            long placeDuration = placeDurationOpt.map(PlaceDuration::getDuration).orElse(0L);
            totalDurationVisited += placeDuration;
        }
        // Remove the last semicolon
        if (coordinates.length() > 0) {
            coordinates.setLength(coordinates.length() - 1);
        }
        JSONObject distanceMatrixResponse = tableService.generateTravelTimeMatrix(coordinates.toString(), "car", "distance,duration");
        List<List<Double>> distanceMatrix = extractDistanceMatrix(distanceMatrixResponse);
        List<List<Double>> durationMatrix = extractDurationMatrix(distanceMatrixResponse);
        List<Integer> optimalRouteIndices = tspService.findMinDistanceRoute(distanceMatrix);
        List<Place> orderedPlaces = new ArrayList<>();
        double totalDuration = 0.0;
        Integer previousIndex = null;
        for (Integer currentIndex : optimalRouteIndices) {
            if (previousIndex != null) {
                totalDuration += durationMatrix.get(previousIndex).get(currentIndex);
                totalDistance += distanceMatrix.get(previousIndex).get(currentIndex);
            }
            orderedPlaces.add(placeIndexToName.get(currentIndex));
            previousIndex = currentIndex;
        }
        int hours = (int) (totalDuration / 3600);
        int minutes = (int) ((totalDuration % 3600) / 60);
        String formattedDuration = String.format("%d hours %d minutes", hours, minutes);
        System.out.println("Total Duration: " + formattedDuration);
        // Create circuit metadata
        Map<String, Object> circuitData = new HashMap<>();
        circuitData.put("circuit", orderedPlaces.stream().map(placeMapper).toList());
        circuitData.put("totalGlobalPlaces", orderedPlaces.size());
        circuitData.put("totalPlacesInCity", placesCountByCity);
        circuitData.put("totalDuration", formattedDuration);
        circuitData.put("totalDistance", totalDistance);

        allCircuits.add(circuitData);
    }


    // Before returning
    System.out.println("All Circuits: " + allCircuits);
    Map<String, Object> response = new HashMap<>();
    response.put("allCircuits", allCircuits);
    return response;
}
    @Override
    @Transactional
    public void updateCitySummaries(List<String> citiesVisited) {
        for (String cityName : citiesVisited) {
            City city = cityRepository.findByName(cityName);
            if (city != null) {
                // Find the existing CitySummary for this city
                Optional<CitySummary> citySummaryOpt = citySummaryRepository.findByCity(city);
                CitySummary citySummary;

                if (citySummaryOpt.isPresent()) {
                    citySummary = citySummaryOpt.get();
                } else {
                    // Create a new CitySummary if not found
                    citySummary = new CitySummary();
                    citySummary.setCity(city);
                }
                // Calculate totalPlaces and totalDuration
                List<Place> placesInCity = placeRepository.findByCityId(city.getId());
                long totalPlaces = placesInCity.size();
                long totalDurationInSeconds = placesInCity.stream()
                        .map(place -> placeDurationRepository.findByPlace(place))
                        .filter(Optional::isPresent)
                        .mapToLong(placeDurationOpt -> placeDurationOpt.get().getDuration())
                        .sum();

                // Convert duration from seconds to days
                long totalDurationInDays = totalDurationInSeconds / 10800;// 86400 seconds in a day
                citySummary.setTotalPlaces(totalPlaces);
                citySummary.setTotalDuration(totalDurationInDays);
                citySummaryRepository.save(citySummary);
            } else {
                System.out.println("City not found: " + cityName);
            }
        }
    }

    public class ProbabilityTable {
        private static final Map<Integer, Map<Integer, Integer>> probabilityTable = new HashMap<>();

        static {
            // Initialize probabilities for [cities][days] -> number of probability circuits
            probabilityTable.put(1, Map.of(1, 1, 2, 1, 3, 1, 4, 1, 5, 1));
            probabilityTable.put(2, Map.of(1, 1, 2, 2, 3, 3, 4, 3, 5, 4, 7, 5));
            probabilityTable.put(3, Map.of(1, 1, 2, 2, 3, 4, 4, 5, 5, 6, 7, 7));
            probabilityTable.put(4, Map.of(1, 1, 2, 3, 3, 5, 4, 7, 5, 9, 6, 10, 7, 12));
            probabilityTable.put(5, Map.of(1, 1, 2, 4, 3, 6, 4, 9, 5, 12, 6, 14, 7, 16, 8, 18));
            probabilityTable.put(6, Map.of(1, 1, 2, 5, 3, 8, 4, 11, 5, 14, 6, 17, 7, 19, 8, 21, 10, 24));
            probabilityTable.put(7, Map.of(1, 1, 2, 6, 3, 9, 4, 12, 5, 16, 6, 19, 7, 22, 8, 25, 9, 27, 10, 29));
            probabilityTable.put(8, Map.of(1, 1, 2, 7, 3, 10, 4, 13, 5, 17, 6, 20, 7, 24, 8, 27, 9, 30, 10, 33));
            probabilityTable.put(10, Map.of(1, 1, 2, 8, 3, 12, 4, 16, 5, 20, 6, 24, 7, 28, 8, 31, 9, 34, 10, 36));
        }

        public static int getProbabilityCircuits(int cities, int days) {
            return probabilityTable
                    .getOrDefault(cities, Map.of()) // Find the city map
                    .getOrDefault(days, 1);         // Find the days value or default to 1
        }
    }

    public List<Map<String, Integer>> generatePlans(
            int remainingDays,
            String[] cities,
            List<Integer> minDaysPerCity,
            int[] maxDaysPerCity,
            List<Map<String, Integer>> allPlans) {

        System.out.println("Current remainingDays: " + remainingDays + ", minDaysPerCity: " + minDaysPerCity);

        // Base case: if remaining days are zero, check if the plan is valid
        if (remainingDays == 0) {
            boolean validPlan = true;
            for (int i = 0; i < cities.length; i++) {
                if (minDaysPerCity.get(i) < 0 || minDaysPerCity.get(i) > maxDaysPerCity[i]) {
                    validPlan = false;
                    break;
                }
            }

            if (validPlan) {
                Map<String, Integer> completePlan = new HashMap<>();
                for (int i = 0; i < cities.length; i++) {
                    completePlan.put(cities[i], minDaysPerCity.get(i));
                }
                allPlans.add(completePlan);
                System.out.println("Adding completePlan to allPlans: " + completePlan);
            }
            return allPlans;
        }

        // Edge case: if remainingDays is less than zero, stop recursion
        if (remainingDays < 0) {
            return allPlans;
        }

        // Recursive case: distribute remaining days among cities
        for (int i = 0; i < cities.length; i++) {
            if (minDaysPerCity.get(i) < maxDaysPerCity[i]) {
                // Increment the minimum days for the current city
                minDaysPerCity.set(i, minDaysPerCity.get(i) + 1);
                // Recur with one less remaining day
                generatePlans(remainingDays - 1, cities, minDaysPerCity, maxDaysPerCity, allPlans);
                // Backtrack: decrement the minimum days for the current city
                minDaysPerCity.set(i, minDaysPerCity.get(i) - 1);
            }
        }
        System.out.println("Returning from recursive case, allPlans size: " + allPlans.size());
        return allPlans;
    }

    public List<List<Double>> extractDistanceMatrix(JSONObject distanceMatrixResponse) {
        List<List<Double>> distanceMatrix = new ArrayList<>();
        JSONArray distances = distanceMatrixResponse.getJSONArray("distances");
        for (int i = 0; i < distances.length(); i++) {
            JSONArray row = distances.getJSONArray(i);
            List<Double> rowList = new ArrayList<>();
            for (int j = 0; j < row.length(); j++) {
                rowList.add(row.getDouble(j));
            }
            distanceMatrix.add(rowList);
        }
        return distanceMatrix;
    }
    public List<List<Double>> extractDurationMatrix(JSONObject distanceMatrixResponse) {
        List<List<Double>> durationMatrix = new ArrayList<>();
        JSONArray durations = distanceMatrixResponse.getJSONArray("durations");
        for (int i = 0; i < durations.length(); i++) {
            JSONArray row = durations.getJSONArray(i);
            List<Double> rowList = new ArrayList<>();
            for (int j = 0; j < row.length(); j++) {
                rowList.add(row.getDouble(j));
            }
            durationMatrix.add(rowList);
        }
        return durationMatrix;
    }

}


