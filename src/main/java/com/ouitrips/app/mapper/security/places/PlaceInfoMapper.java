package com.ouitrips.app.mapper.security.places;

import com.ouitrips.app.entities.places.PlaceInfo;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class PlaceInfoMapper implements Function<PlaceInfo, Object> {

    @Override
    public Object apply(PlaceInfo placeInfo) {
        Map<String, Object> response = new HashMap<>();
        if (placeInfo == null) {
            return response;
        }
        response.put("reference", placeInfo.getId());
        response.put("phone", placeInfo.getPhone());
        response.put("phone2", placeInfo.getPhone2());
        response.put("content", placeInfo.getContent());
        response.put("website", placeInfo.getWebsite());
        response.put("address", placeInfo.getAddress());

        return response;
    }
}
