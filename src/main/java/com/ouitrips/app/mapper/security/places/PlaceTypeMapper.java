package com.ouitrips.app.mapper.security.places;

import com.ouitrips.app.entities.places.PlaceType;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class PlaceTypeMapper implements Function<PlaceType, Object> {

    @Override
    public Object apply(PlaceType placeType) {
        Map<String, Object> response = new HashMap<>();
        if (placeType == null) {
            return response;
        }
        response.put("reference", placeType.getId());
        response.put("name", placeType.getName());
        response.put("code", placeType.getCode());
        return response;
    }
}
