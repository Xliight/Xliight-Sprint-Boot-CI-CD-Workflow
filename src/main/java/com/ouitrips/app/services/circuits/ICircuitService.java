package com.ouitrips.app.services.circuits;

import jakarta.transaction.Transactional;
import java.util.Map;

public interface ICircuitService {
    Object save(Map<String, Object> params);
    Object getAllByUser();
    Object getAll();
    Object get(Integer id);
    void delete(Integer id);
    @Transactional
    Object getLiveCircuits();
    @Transactional
    Object search(String query, Map<String, Map<String, Object>> variables);
}
