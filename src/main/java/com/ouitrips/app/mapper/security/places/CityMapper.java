package com.ouitrips.app.mapper.security.places;

import com.ouitrips.app.entities.places.City;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class CityMapper implements Function<City, Object> {

    @Override
    public Object apply(City city) {
        Map<String, Object> response = new HashMap<>();
        if (city == null) {
            return response;
        }
        response.put("reference", city.getId());
        response.put("name", city.getName());
        response.put("latitude", city.getLatitude());
        response.put("longitude", city.getLongitude());
        response.put("temperature", city.getTemperature());
        response.put("gps", city.getGps());

        return response;
    }
}
