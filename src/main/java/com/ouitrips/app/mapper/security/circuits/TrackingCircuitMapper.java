package com.ouitrips.app.mapper.security.circuits;

import com.ouitrips.app.entities.circuits.TrackingCircuit;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class TrackingCircuitMapper implements Function<TrackingCircuit, Object> {
    @Override
    public Object apply(TrackingCircuit trackingCircuit) {
        Map<String, Object> response = new HashMap<>();
        if (trackingCircuit == null) {
            return response;
        }
        response.put("reference", trackingCircuit.getId());
        response.put("startDate", trackingCircuit.getStartDate() != null ? trackingCircuit.getStartDate().toString() : null);
        response.put("updatedDate", trackingCircuit.getUpdatedDate() != null ? trackingCircuit.getUpdatedDate().toString() : null);
        return response;
    }
}
