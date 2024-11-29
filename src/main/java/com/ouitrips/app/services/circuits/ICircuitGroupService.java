package com.ouitrips.app.services.circuits;

import java.util.Map;

public interface ICircuitGroupService {
    Object save(Map<String, Object> params);
    void delete(Integer id);
    Object get(Integer id);
    Object getAll();
}
