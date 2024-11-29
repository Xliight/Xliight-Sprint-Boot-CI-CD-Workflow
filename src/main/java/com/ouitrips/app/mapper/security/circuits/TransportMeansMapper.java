package com.ouitrips.app.mapper.security.circuits;

import com.ouitrips.app.entities.circuits.TransportMeans;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class TransportMeansMapper implements Function<TransportMeans, Object> {
    @Override
    public Object apply(TransportMeans transportMeans) {
        Map<String, Object> response = new HashMap<>();
        if (transportMeans == null) {
            return response;
        }
        response.put("reference", transportMeans.getId());
        response.put("name", transportMeans.getName());
        response.put("code", transportMeans.getCode());
        return response;
    }
}
