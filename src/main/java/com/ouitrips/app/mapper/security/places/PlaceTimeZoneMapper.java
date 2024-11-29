package com.ouitrips.app.mapper.security.places;


import com.ouitrips.app.entities.places.PlaceTimeZone;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class PlaceTimeZoneMapper implements Function<PlaceTimeZone, Object> {

    @Override
    public Object apply(PlaceTimeZone placeTimeZone) {
        Map<String, Object> response = new HashMap<>();
        if (placeTimeZone == null) {
            return response;
        }
        response.put("reference", placeTimeZone.getId());
        response.put("utc_offset", placeTimeZone.getUtcOffset());
        response.put("name", placeTimeZone.getName());
        return response;
    }
}
