package com.ouitrips.app.services.places.impl;

import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.googlemapsservice.DirectionsApi;
import com.ouitrips.app.googlemapsservice.DistanceMatrixApiRequest;
import com.ouitrips.app.googlemapsservice.GeoApiContext;
import com.ouitrips.app.googlemapsservice.errors.ApiException;
import com.ouitrips.app.googlemapsservice.model.*;
import com.ouitrips.app.services.places.GoogleDistanceService;
import com.ouitrips.app.utils.VariableProperty;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
@AllArgsConstructor
public class GoogleDistanceServiceImpl implements GoogleDistanceService {

    private final VariableProperty variableProperty;
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDistanceServiceImpl.class);

    @Override
    public DistanceMatrix calcDistance(LatLng[] latLngsOrigins,
                                       LatLng[] latLngsDestinations,
                                       TravelMode mode,
                                       String language,
                                       DirectionsApi.RouteRestriction routeRestriction,
                                       Instant departureTime,
                                       Instant arrivalTime,
                                       TrafficModel trafficModel,
                                       TransitMode transitMode,
                                       TransitRoutingPreference transitRoutingPreference,
                                       Unit unit
    ) {//language: "en-US"
        System.out.println("latLngsOrigins "+latLngsOrigins.length);
        System.out.println("latLngsDestinatons "+latLngsDestinations.length);
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(variableProperty.getApiKeyGoogle())
                .build();
        DistanceMatrix distanceMatrix = null;
        DistanceMatrixApiRequest matrixApiRequest = new DistanceMatrixApiRequest(context);
        if(latLngsOrigins != null){
            matrixApiRequest.origins(latLngsOrigins);
        }else{
            //todo throw exception

        }
        if(latLngsDestinations != null){
            matrixApiRequest.destinations(latLngsDestinations);
        }else{
            //todo throw exception
        }
        if(unit != null){
            matrixApiRequest.units(unit);
        }else {
            //Todo set a default unit

        }
        if(mode != null){
            matrixApiRequest.mode(mode);
        }else {
            //Todo set a default travel mode
//            matrixApiRequest.mode(mode);
        }
        try {
            distanceMatrix = matrixApiRequest.await();
        }catch (InterruptedException e) { // Compliant; the interrupted state is restored
            LOGGER.error("Thread was interrupted while awaiting distance matrix", e);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            LOGGER.error("IO Exception occurred while fetching distance matrix", e);
            throw new ExceptionControllerAdvice.GeneralException("An error occurred: IO exception");
        } catch (ApiException e) {
            LOGGER.error("IO Exception occurred while fetching distance matrix", e);
            throw new ExceptionControllerAdvice.GeneralException("An error occurred: api exception");
        }finally {
            context.shutdown();
        }
        return distanceMatrix;
    }
    @Override
    public long[][] processDistanceMatrix(DistanceMatrix distanceMatrix) {
        int size = distanceMatrix.rows.length;
        long[][] distanceArray = new long[size][size];

        for (int i = 0; i < size; i++) {
            DistanceMatrixRow row = distanceMatrix.rows[i];
            for (int j = 0; j < size; j++) {
                DistanceMatrixElement element = row.elements[j];
                if (element.status == DistanceMatrixElementStatus.OK) {
                    distanceArray[i][j] = Math.round(element.distance.inMeters / 1000.0); // Convert to km
                } else {
                    distanceArray[i][j] = Long.MAX_VALUE;
                }
            }
        }

        return distanceArray;
    }
}
