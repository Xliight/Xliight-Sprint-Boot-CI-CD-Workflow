package com.ouitrips.app.services.circuits;

import java.util.List;
import java.util.Map;

public interface ILocationService {
    Object save(Map<String, Object> params);
    void delete(Integer id);
    Object get(Integer id);
    List<Object> getLocationByStep(Integer stepId);
    Object getAll();
}
