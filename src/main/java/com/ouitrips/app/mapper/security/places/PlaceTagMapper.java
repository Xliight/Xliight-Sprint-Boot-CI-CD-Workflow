package com.ouitrips.app.mapper.security.places;

import com.ouitrips.app.entities.places.PlaceTag;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class PlaceTagMapper implements Function<PlaceTag, Object> {

    @Override
    public Object apply(PlaceTag placeTag) {
        Map<String, Object> response = new HashMap<>();
        if (placeTag == null) {
            return response;
        }
        response.put("reference", placeTag.getId());
        response.put("tag", placeTag.getTag());
        response.put("category", placeTag.getCategory());
        return response;
    }
}
