package com.ouitrips.app.services.places;

import com.ouitrips.app.googlemapsservice.DirectionsApi;
import com.ouitrips.app.googlemapsservice.model.*;

import java.time.Instant;

public interface GoogleDistanceService {

    DistanceMatrix calcDistance(LatLng[] latLngsOrigins,
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
    );

    long[][] processDistanceMatrix(DistanceMatrix distanceMatrix);

}
