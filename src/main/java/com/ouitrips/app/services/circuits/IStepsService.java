package com.ouitrips.app.services.circuits;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface IStepsService {
    Object save(Map<String, Object> params);
    void delete(Integer id);
    Object get(Integer id);
    Object getAll();
    @Transactional
    List<Object> getStepByCircuit(Integer circuitReference);
}
