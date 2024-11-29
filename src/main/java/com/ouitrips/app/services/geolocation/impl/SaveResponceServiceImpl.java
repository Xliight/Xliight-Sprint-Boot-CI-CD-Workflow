package com.ouitrips.app.services.geolocation.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouitrips.app.entities.geolocation.*;
import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.entities.places.PlaceInfoGeolocation;
import com.ouitrips.app.googlemapsservice.DirectionsApi;
import com.ouitrips.app.googlemapsservice.model.*;
import com.ouitrips.app.mapper.security.geolocation.DistanceMatrixJsonConverter;
import com.ouitrips.app.repositories.security.geolocation.*;
import com.ouitrips.app.repositories.security.places.PlaceRepository;
import com.ouitrips.app.services.geolocation.ISaveResponseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ouitrips.app.services.places.GoogleDistanceService;
import java.time.Instant;
import java.util.*;

@AllArgsConstructor
@Service
public class SaveResponceServiceImpl implements ISaveResponseService {

    private final ResponseAPIRepository responseAPIRepository;
    private final RequestAPIRepository requestAPIRepository;
    private final MetricDetailsRepository metricDetailsRepository;
    private final RouteRepository routeRepository;
    private final GoogleDistanceService googleDistanceService;
    private final PlaceRepository placeRepository;
    private final PlaceInfoGeolocationRepository placeInfoGeolocationRepository;
    private final ObjectMapper objectMapper; // Injecting ObjectMapper for JSON conversion

    private static final Logger logger = LoggerFactory.getLogger(SaveResponceServiceImpl.class);




    public ResponseAPI calculateAndSaveDistance(RequestAPI requestAPI) {
        validateRequest(requestAPI);

        // Check for existing requests
        ResponseAPI existingResponse = checkForExistingRequests(requestAPI);
        if (existingResponse != null) {
            return existingResponse;
        }

        requestAPIRepository.save(requestAPI);

        // Extract and prepare coordinates
        LatLng[] origins = extractCoordinates(requestAPI.getOriginCoords());
        LatLng[] destinations = extractCoordinates(requestAPI.getDestinationCoords());

        // Calculate the distance matrix
        DistanceMatrix distanceMatrix = calculateDistance(requestAPI, origins, destinations);

        // Create and save the response with custom JSON
        ResponseAPI responseAPI = createCustomJsonResponse(requestAPI, distanceMatrix);
        saveMetricDetailsAndRoutes(distanceMatrix,requestAPI);
        return responseAPI;
    }

    public ResponseAPI createCustomJsonResponse(RequestAPI requestAPI, DistanceMatrix distanceMatrix) {
        ResponseAPI responseAPI = new ResponseAPI();
        responseAPI.setRequestAPI(requestAPI);

        String jsonResponse = DistanceMatrixJsonConverter.convertToCustomJson(distanceMatrix);
        if (jsonResponse != null) {
            responseAPI.setJsonResponse(jsonResponse);
        }

        return responseAPIRepository.save(responseAPI);
    }

    // Validates the input request
    public void validateRequest(RequestAPI requestAPI) {
        if (requestAPI == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
    }

    // Checks for existing requests and responses
    public ResponseAPI checkForExistingRequests(RequestAPI requestAPI) {
        List<RequestAPI> existingRequestAPIs = requestAPIRepository.findByOriginCoordsAndDestinationCoordsAndModeAndLanguageAndRouteRestrictionAndDepartureDateAndArrivalTimeAndTrafficModelAndTrafficTransitAndTransitRoutingPreferenceAndUnit(
                requestAPI.getOriginCoords(),
                requestAPI.getDestinationCoords(),
                requestAPI.getMode(),
                requestAPI.getLanguage(),
                requestAPI.getRouteRestriction(),
                requestAPI.getDepartureDate(),
                requestAPI.getArrivalTime(),
                requestAPI.getTrafficModel(),
                requestAPI.getTrafficTransit(),
                requestAPI.getTransitRoutingPreference(),
                requestAPI.getUnit()
        );

        if (!existingRequestAPIs.isEmpty()) {
            logger.info("Request exists.");
            List<ResponseAPI> existingResponseAPIs = responseAPIRepository.findByRequestAPIIn(existingRequestAPIs);
            if (!existingResponseAPIs.isEmpty()) {
                logger.info("Duplicate request detected. Fetching existing response.");
                return existingResponseAPIs.get(0); // Return the first existing response if found
            }
        }
        return null;
    }

    // Extracts coordinates from a string and returns LatLng array
    public LatLng[] extractCoordinates(String coords) {
        String[] coordArray = coords.split(",");
        return new LatLng[]{
                new LatLng(Double.parseDouble(coordArray[0].trim()), Double.parseDouble(coordArray[1].trim()))
        };
    }

    // Calculates distance using GoogleDistanceService
    public DistanceMatrix calculateDistance(RequestAPI requestAPI, LatLng[] origins, LatLng[] destinations) {
        if (origins == null || destinations == null || requestAPI == null) {
            throw new IllegalArgumentException("Origins, destinations, and requestAPI cannot be null");
        }

        TravelMode travelMode = null;
        if (requestAPI.getMode() != null) {
            travelMode = TravelMode.valueOf(requestAPI.getMode().toUpperCase());
        } else {
            travelMode = TravelMode.DRIVING; // Default mode if not provided
        }

        String language = requestAPI.getLanguage() != null ? requestAPI.getLanguage() : "en"; // Default to English

        DirectionsApi.RouteRestriction routeRestriction = null;
        if (requestAPI.getRouteRestriction() != null) {
            routeRestriction = DirectionsApi.RouteRestriction.valueOf(requestAPI.getRouteRestriction().toUpperCase());
        }

        Instant departureTime = requestAPI.getDepartureDate(); // Assuming this can be null; handle accordingly
        Instant arrivalTime = requestAPI.getArrivalTime(); // Assuming this can be null; handle accordingly

        TrafficModel trafficModel = null;
        if (requestAPI.getTrafficModel() != null) {
            trafficModel = TrafficModel.valueOf(requestAPI.getTrafficModel().toUpperCase());
        }

        TransitMode transitMode = null;
        if (requestAPI.getTrafficTransit() != null) {
            transitMode = TransitMode.valueOf(requestAPI.getTrafficTransit().toUpperCase());
        }

        TransitRoutingPreference transitRoutingPreference = null;
        if (requestAPI.getTransitRoutingPreference() != null) {
            transitRoutingPreference = TransitRoutingPreference.valueOf(requestAPI.getTransitRoutingPreference().toUpperCase());
        }

        Unit unit = null;
        if (requestAPI.getUnit() != null) {
            unit = Unit.valueOf(requestAPI.getUnit().toUpperCase());
        }

        // Call the distance calculation service with all the validated parameters
        return googleDistanceService.calcDistance(
                origins,
                destinations,
                travelMode,
                language,
                routeRestriction,
                departureTime,
                arrivalTime,
                trafficModel,
                transitMode,
                transitRoutingPreference,
                unit
        );
    }


    // Saves metric details and routes based on the distance matrix
    private void saveMetricDetailsAndRoutes(DistanceMatrix distanceMatrix,RequestAPI requestAPI) {
        for (int i = 0; i < distanceMatrix.originAddresses.length; i++) {
            Place originPlace = createOrUpdatePlace(distanceMatrix.originAddresses[i]);
            Place destinationPlace = createOrUpdatePlace(distanceMatrix.destinationAddresses[i]);

            // Create and save MetricDetails
            MetricDetails metricDetails = new MetricDetails();
            metricDetails.setOrigin(originPlace);
            metricDetails.setDestination(destinationPlace);
            metricDetails = metricDetailsRepository.save(metricDetails);

            // Create and save PlaceInfoGeolocation for the origin and destination places
            createAndSavePlaceInfoGeolocation(originPlace, distanceMatrix.originAddresses[i]);
            createAndSavePlaceInfoGeolocation(destinationPlace, distanceMatrix.destinationAddresses[i]);

            // Create and save Routes for each row
            saveRoutesForRow(distanceMatrix.rows[i], requestAPI, metricDetails);
        }
    }

    // Saves routes for a specific row of the distance matrix
    private void saveRoutesForRow(DistanceMatrixRow row, RequestAPI requestAPI, MetricDetails metricDetails) {
        for (int j = 0; j < row.elements.length; j++) {
            DistanceMatrixElement element = row.elements[j];
            if ("OK".equals(element.status.toString())) {
                Route route = new Route();

                // Set human-readable and value fields for distance and duration
                RouteMetric distanceMetric = new RouteMetric();
                distanceMetric.setText(element.distance.humanReadable);
                distanceMetric.setValue(element.distance.inMeters); // Numeric value in meters
                route.setDistance(distanceMetric);

                RouteMetric durationMetric = new RouteMetric();
                durationMetric.setText(element.duration.humanReadable);
                durationMetric.setValue(element.duration.inSeconds); // Numeric value in seconds
                route.setDuration(durationMetric);

                if (element.durationInTraffic != null) {
                    RouteMetric durationInTrafficMetric = new RouteMetric();
                    durationInTrafficMetric.setText(element.durationInTraffic.humanReadable);
                    durationInTrafficMetric.setValue(element.durationInTraffic.inSeconds);
                    route.setDurationInTraffic(durationInTrafficMetric);
                }

                route.setRequestAPI(requestAPI);
                route.setMetricDetails(metricDetails);
                routeRepository.save(route);
            }
        }
    }

    // Helper method to create or update Place entities
    private Place createOrUpdatePlace(String address) {
        Optional<Place> optionalPlace = placeRepository.findByAddress(address);
        Place place;

        if (optionalPlace.isPresent()) {
            logger.info("Place found: {}", optionalPlace.get().getId());
            place = optionalPlace.get();
        } else {
            logger.info("Place not found, creating a new one.");
            place = new Place();
            place.setAddress(address);
            place = placeRepository.save(place);
        }

        return place;
    }

    // Creates and saves PlaceInfoGeolocation for the specified place
    private void createAndSavePlaceInfoGeolocation(Place place, String address) {
        Optional<PlaceInfoGeolocation> existingPlaceInfoGeo = placeInfoGeolocationRepository.findByPlace(place);
        if (existingPlaceInfoGeo.isEmpty()) {
            PlaceInfoGeolocation placeInfoGeolocation = new PlaceInfoGeolocation();
            placeInfoGeolocation.setPlace(place);
            placeInfoGeolocation.setPlaceinfo(address);
            placeInfoGeolocationRepository.save(placeInfoGeolocation);
        } else {
            logger.info("PlaceInfoGeolocation already exists for place ID: {}", place.getId());
        }
    }





  }
