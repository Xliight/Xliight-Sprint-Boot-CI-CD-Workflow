package com.ouitrips.app.web.circuits;

import com.ouitrips.app.dtos.SearchCircuitMapper;
import com.ouitrips.app.dtos.SearchModelMapper;
import com.ouitrips.app.dtos.circuitdto.SearchModelDTO;
import com.ouitrips.app.entities.circuits.SearchModel;
import com.ouitrips.app.repositories.security.circuits.SearchModelRepository;
import com.ouitrips.app.services.circuits.impl.SearchCircuitServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${REST_NAME}/public/search_circuits")
@AllArgsConstructor

public class SearchCircuitController {

    private final SearchCircuitServiceImpl searchCircuitService;
    private final SearchCircuitMapper searchCircuitMapper;
    private final SearchModelMapper searchModelMapper;
    private final SearchModelRepository searchModelRepository;
    private final SearchCircuitServiceImpl searchCircuitServiceImpl;

    // Create a new SearchCircuit
    @PostMapping
    public ResponseEntity<List<String>> createSearchCircuit(@RequestBody List<SearchModel> searchCircuit) {

        List<String> createdSearchCircuit = searchCircuitService.saveSearchModels(searchCircuit);
        return ResponseEntity.ok(createdSearchCircuit);
    }


    // Get a SearchCircuit by ID
    @GetMapping("/{id}")
    public ResponseEntity<SearchModelDTO> getSearchCircuit(@PathVariable Integer id) {
        SearchModel searchmodel = searchCircuitService.getSearchCircuit(id);
        SearchModelDTO searchCircuitDTO=searchModelMapper.toSearchModelDTO(searchmodel);
        return ResponseEntity.ok(searchCircuitDTO);
    }

    @GetMapping("/ref/{reference}")
    public ResponseEntity<SearchModel> getSearchCircuitByRef(@PathVariable String reference) {
        SearchModel searchCircuit = searchCircuitService.getSearchCircuitByRef(reference);
        return ResponseEntity.ok(searchCircuit);
    }

    // Update a SearchCircuit


    // Soft delete a SearchCircuit (set isDeleted to true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchCircuit(@PathVariable Integer id) {
        searchCircuitService.deleteSearchCircuit(id);
        return ResponseEntity.noContent().build();
    }

    // Get all non-deleted SearchCircuits
    @GetMapping
    public ResponseEntity<List<SearchModel>> getAllSearchCircuits() {
        List<SearchModel> searchCircuits = searchCircuitService.getAllSearchCircuits();
        return ResponseEntity.ok(searchCircuits);
    }

}

