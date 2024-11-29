package com.ouitrips.app.web.circuits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.circuits.ICircuitTrackingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/circuits/tracking")
@AllArgsConstructor
public class CircuitTrackingController {
    private final ICircuitTrackingService circuitTrackingService;


    @PostMapping("/save")
    public ResponseEntity<?> saveTrackingCircuit(
            @RequestParam(value = "circuitReference", required = false) Integer circuitReference,
            @RequestParam(name = "places", required = false) String places
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("circuitReference", circuitReference);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> columns = null;
        try {
            columns = places != null ?
                    objectMapper.readValue(places, new TypeReference<List<Map<String, Object>>>() {}) : new ArrayList<>();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Can't convert params 'places' to object");
        }

        params.put("places", columns);

        return Response.responseData(circuitTrackingService.save(params));
    }

    @GetMapping("/get_by_circuit")
    public ResponseEntity<?> getTrackingAllByCircuit(@RequestParam Integer circuitReference) {
        return ResponseEntity.ok(circuitTrackingService.getTrackingAllByCircuit(circuitReference));
    }
}
