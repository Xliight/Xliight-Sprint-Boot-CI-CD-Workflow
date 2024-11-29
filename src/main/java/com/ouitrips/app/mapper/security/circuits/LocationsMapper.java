package com.ouitrips.app.mapper.security.circuits;

import com.ouitrips.app.entities.circuits.Location;
import com.ouitrips.app.mapper.security.places.PlaceMapper;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class LocationsMapper implements Function<Location, Object> {
    private final PlaceMapper placeMapper;

    public LocationsMapper(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    @Override
    public Object apply(Location locations) {
        Map<String, Object> response = new HashMap<>();
        if (locations == null)
            return response;
        response.put("reference", locations.getId());
        response.put("description", locations.getDescription());
        response.put("latitude", locations.getLatitude());
        response.put("longitude", locations.getLongitude());
        response.put("location", locations.getAddress());
        response.put("name", locations.getName());
        response.put("rating", locations.getRating());
        response.put("place", placeMapper.apply(locations.getPlace()));
        return response;
    }
}