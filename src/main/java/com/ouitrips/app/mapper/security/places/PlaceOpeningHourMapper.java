package com.ouitrips.app.mapper.security.places;


import com.ouitrips.app.entities.places.PlaceOpeningHour;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class PlaceOpeningHourMapper implements Function<PlaceOpeningHour, Object> {
    @Override
    public Object apply(PlaceOpeningHour placeOpeningHour) {
        Map<String, Object> response = new HashMap<>();
        if (placeOpeningHour == null) {
            return response;
        }
        response.put("reference", placeOpeningHour.getId());
        response.put("opening_time", placeOpeningHour.getOpeningTime());
        response.put("close_time", placeOpeningHour.getCloseTime());

        return response;
    }
}
