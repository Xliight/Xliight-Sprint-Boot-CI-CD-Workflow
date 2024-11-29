package com.ouitrips.app.mapper.security.places;


import com.ouitrips.app.entities.places.PlaceDuration;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;


@Configuration
public class PlaceDurationMapper implements Function<PlaceDuration, Object> {

    @Override
    public Object apply(PlaceDuration placeDuration) {
        Map<String, Object> response = new HashMap<>();
        if (placeDuration == null) {
            return response;
        }
        response.put("reference", placeDuration.getId());
        response.put("duration", placeDuration.getDuration());
        return response;
    }


}
