package com.ouitrips.app.mapper.security.circuits;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.circuits.Step;
import com.ouitrips.app.repositories.security.circuits.StepsRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class CircuitMapper  implements Function<Circuit,Object> {

    private final StepsRepository stepsRepository;
    private final StepsMapper stepsMapper;

    public CircuitMapper(StepsRepository stepsRepository, StepsMapper stepsMapper) {
        this.stepsRepository = stepsRepository;
        this.stepsMapper = stepsMapper;
    }
    @Override
    public Object apply(Circuit circuits) {
        Map<String, Object> response = new HashMap<>();
        if(circuits == null)
            return response;
        response.put("reference", circuits.getId());
        response.put("name", circuits.getName());
        response.put("distance", circuits.getDistance());
        return response;
    }
    public Object applyDetail(Circuit circuit) {
        Map<String, Object> response = new HashMap<>();
        if(circuit == null)
            return response;
        response = (Map<String, Object>) this.apply(circuit);
        Sort sortByOrder = Sort.by(Sort.Direction.ASC, "orderStep");
        List<Step> steps = stepsRepository.findByCircuit(circuit, sortByOrder);
        response.put("steps", steps.stream().map(stepsMapper).toList());
        return response;
    }
}
