package com.ouitrips.app.mapper.security.geolocation;

import com.ouitrips.app.googlemapsservice.model.DistanceMatrix;
import com.ouitrips.app.googlemapsservice.model.DistanceMatrixElement;
import com.ouitrips.app.googlemapsservice.model.DistanceMatrixRow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DistanceMatrixJsonConverter {

    public DistanceMatrixJsonConverter() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(DistanceMatrixJsonConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String VALUE_KEY = "value";

    /**
     * Converts a DistanceMatrix object to a custom JSON format.
     *
     * @param distanceMatrix DistanceMatrix object to convert.
     * @return Custom JSON as a String.
     */
    public static String convertToCustomJson(DistanceMatrix distanceMatrix) {
        Map<String, Object> customResponse = new HashMap<>();
        customResponse.put("destination_addresses", distanceMatrix.destinationAddresses);
        customResponse.put("origin_addresses", distanceMatrix.originAddresses);

        List<Map<String, Object>> rowsList = new ArrayList<>();
        for (DistanceMatrixRow row : distanceMatrix.rows) {
            Map<String, Object> rowMap = new HashMap<>();
            List<Map<String, Object>> elementsList = new ArrayList<>();

            for (DistanceMatrixElement element : row.elements) {
                Map<String, Object> elementMap = new HashMap<>();
                elementMap.put("status", element.status.toString());

                if (element.distance != null) {
                    Map<String, Object> distanceMap = new HashMap<>();
                    distanceMap.put("text", element.distance.humanReadable);
                    distanceMap.put(VALUE_KEY, element.distance.inMeters);
                    elementMap.put("distance", distanceMap);
                }

                if (element.duration != null) {
                    Map<String, Object> durationMap = new HashMap<>();
                    durationMap.put("text", element.duration.humanReadable);
                    durationMap.put(VALUE_KEY, element.duration.inSeconds);
                    elementMap.put("duration", durationMap);
                }

                if (element.durationInTraffic != null) {
                    Map<String, Object> durationTrafficMap = new HashMap<>();
                    durationTrafficMap.put("text", element.durationInTraffic.humanReadable);
                    durationTrafficMap.put(VALUE_KEY, element.durationInTraffic.inSeconds);
                    elementMap.put("duration_in_traffic", durationTrafficMap);
                }

                elementsList.add(elementMap);
            }

            rowMap.put("elements", elementsList);
            rowsList.add(rowMap);
        }

        customResponse.put("rows", rowsList);
        customResponse.put("status", "OK");

        try {
            return objectMapper.writeValueAsString(customResponse);
        } catch (JsonProcessingException e) {
            logger.error("Error converting custom response to JSON", e);
            return null;
        }
    }

    /**
     * Converts a custom JSON string back into a DistanceMatrix model.
     *
     * @param jsonResponse Custom JSON string.
     * @return DistanceMatrix object.
     */
    public static DistanceMatrix convertFromCustomJson(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, DistanceMatrix.class);
        } catch (JsonProcessingException e) {
            logger.error("Error converting JSON to DistanceMatrix model", e);
            return null;
        }
    }
}
