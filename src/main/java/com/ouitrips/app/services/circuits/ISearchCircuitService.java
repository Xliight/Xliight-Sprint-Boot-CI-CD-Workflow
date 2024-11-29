package com.ouitrips.app.services.circuits;

import com.ouitrips.app.entities.circuits.SearchCircuit;
import com.ouitrips.app.entities.circuits.SearchModel;

import java.util.List;
import java.util.Optional;

public interface ISearchCircuitService {
//    SearchCircuit createSearchCircuit(SearchCircuit searchCircuit);
    SearchModel getSearchCircuit(Integer id);

    void deleteSearchCircuit(Integer id);

    List<SearchModel> getAllSearchCircuits();

}
