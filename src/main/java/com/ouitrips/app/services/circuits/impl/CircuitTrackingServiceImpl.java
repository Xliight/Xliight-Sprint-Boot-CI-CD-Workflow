package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.circuits.Step;
import com.ouitrips.app.entities.circuits.TrackingCircuit;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.TrackingCircuitMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.repositories.security.circuits.StepsRepository;
import com.ouitrips.app.repositories.security.circuits.TrackingCircuitRepository;
import com.ouitrips.app.services.circuits.ICircuitTrackingService;
import com.ouitrips.app.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class CircuitTrackingServiceImpl implements ICircuitTrackingService {
    private final TrackingCircuitRepository trackingCircuitRepository;
    private final CircuitRepository circuitRepository;
    private final StepsRepository stepsRepository;
    private final TrackingCircuitMapper trackingCircuitMapper;
    private final DateUtil dateUtil;


    @Override
    @Transactional
    public Object save(Map<String, Object> params) {
        Integer circuitReference = (Integer) params.get("circuitReference");

        if (circuitReference != null) {
            Circuit circuit = circuitRepository.findById(circuitReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.ObjectNotFoundException("Circuit not found"));
            List<Map<String, Object>> places = (List<Map<String, Object>>) params.get("places");

            if (places != null) {
                for (Map<String, Object> columnData : places) {
                    String dateStr = (String) columnData.get("date");
                    String latitude = (String) columnData.get("latitude");
                    String longitude = (String) columnData.get("longitude");
                    Integer stepReference = (Integer) columnData.get("stepReference");

                    Instant date = dateUtil.createDateWithFormat(dateStr).toInstant();

                    TrackingCircuit trackingCircuit = new TrackingCircuit();
                    trackingCircuit.setStartDate(date);
                    trackingCircuit.setUpdatedDate(date);
                    trackingCircuit.setLatitude(latitude);
                    trackingCircuit.setLongitude(longitude);
                    trackingCircuit.setCircuit(circuit);

                    if (stepReference != null) {
                        Step step = stepsRepository.findById(stepReference)
                                .orElseThrow(() -> new ExceptionControllerAdvice.ObjectNotFoundException("Step not found"));
                        trackingCircuit.setStep(step);
                    }

                    trackingCircuitRepository.save(trackingCircuit);
                }
            }
        }

        return Map.of("message", "Tracking circuits saved successfully");
    }

    @Override
    @Transactional
    public List<Object> getTrackingAllByCircuit(Integer circuitReference) {
        List<TrackingCircuit> trackingCircuits = trackingCircuitRepository.findByCircuitIdOrderByStartDate(circuitReference);
        return trackingCircuits.stream()
                .map(trackingCircuitMapper)
                .toList();
    }

    //Live circuit
    //Get current step by circuit
    //todo: Get current step by user
}
