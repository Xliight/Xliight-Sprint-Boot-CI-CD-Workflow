package com.ouitrips.app.mapper.security.places;


import com.ouitrips.app.entities.places.PlacePricing;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class PlacePricingMapper implements Function<PlacePricing, Object> {

    @Override
    public Object apply(PlacePricing placePricing) {
        Map<String, Object> response = new HashMap<>();
        if (placePricing == null) {
            return response;
        }
        response.put("reference", placePricing.getId());
        response.put("price_min", placePricing.getPriceMin());
        response.put("price_max", placePricing.getPriceMax());
        response.put("price", placePricing.getPrice());
        return response;
    }
}
