package com.ouitrips.app.services.places;


import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface IPlaceService {
    Object save(Map<String, Object> params);
    void delete(Integer id);
    Object get(Integer id);
    Object getAll();

//    Object searchPlace(Map<String, Object> params);
//
//    @Transactional
//    Object searchPlaceTest(Map<String, Object> params);

    @Transactional
    Object searchTestPlace(Map<String, Object> params);

    @Transactional
    void updateCitySummaries(List<String> citiesVisited);
}
