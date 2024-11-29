package com.ouitrips.app.services.circuits;


import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ICircuitTrackingService {
    Object save(Map<String, Object> params);


    @Transactional
    List<Object> getTrackingAllByCircuit(Integer circuitReference);
}
