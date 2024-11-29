package com.ouitrips.app.mapper.security.circuits;

import com.ouitrips.app.entities.circuits.CircuitGroup;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class CircuitGroupMapper implements Function<CircuitGroup, Object> {

    @Override
    public Object apply(CircuitGroup circuitGroup) {
        Map<String, Object> response = new HashMap<>();
        if (circuitGroup == null)
            return response;
        response.put("id", circuitGroup.getId());
        response.put("name", circuitGroup.getName());
        response.put("description", circuitGroup.getDescription());
        response.put("color", circuitGroup.getColor());
        response.put("icon", circuitGroup.getIcon());
        return response;
    }
}

