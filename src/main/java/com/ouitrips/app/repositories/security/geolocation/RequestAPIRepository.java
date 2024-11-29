package com.ouitrips.app.repositories.security.geolocation;

import com.ouitrips.app.entities.geolocation.RequestAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RequestAPIRepository  extends JpaRepository<RequestAPI, Integer> {
    List<RequestAPI> findByOriginCoordsAndDestinationCoordsAndModeAndLanguageAndRouteRestrictionAndDepartureDateAndArrivalTimeAndTrafficModelAndTrafficTransitAndTransitRoutingPreferenceAndUnit(
            String originCoords,
            String destinationCoords,
            String mode,
            String language,
            String routeRestriction,
            Instant departureDate,
            Instant arrivalTime,
            String trafficModel,
            String trafficTransit,
            String transitRoutingPreference,
            String unit
    );
}