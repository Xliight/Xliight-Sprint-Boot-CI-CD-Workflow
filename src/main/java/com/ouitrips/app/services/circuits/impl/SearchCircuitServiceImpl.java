package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.*;
import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.exceptions.agency.AgencyException;
import com.ouitrips.app.repositories.security.circuits.*;
import com.ouitrips.app.repositories.security.places.PlaceRepository;
import com.ouitrips.app.services.circuits.ISearchCircuitService;
import com.ouitrips.app.services.places.impl.PlaceServiceImpl;
import com.ouitrips.app.utils.VariableProperty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class SearchCircuitServiceImpl implements ISearchCircuitService {
    private static final Logger logger = LoggerFactory.getLogger(SearchCircuitServiceImpl.class);


    private static final String ERROR_CIRCUIT = "SearchCircuit not found or is deleted";  // Compliant

    private final SearchCircuitRepository searchCircuitRepository;
    private final SearchModelRepository searchModelRepository;
    private final PlaceRepository placeRepository;
    private final VariableProperty variableProperty;
    private final PlaceServiceImpl placeServiceImpl;
    @PersistenceContext
    private EntityManager entityManager;

    public SearchCircuitServiceImpl(SearchCircuitRepository searchCircuitRepository, SearchModelRepository searchModelRepository,PlaceRepository placeRepository, VariableProperty variableProperty, PlaceServiceImpl placeServiceImpl) {
        this.searchCircuitRepository = searchCircuitRepository;
        this.searchModelRepository = searchModelRepository;
        this.placeRepository = placeRepository;
        this.variableProperty = variableProperty;
        this.placeServiceImpl = placeServiceImpl;
    }

    @Transactional
    public List<String> saveSearchModels(List<SearchModel> searchModelsRequest) {
        List<String> savedSearchModelReferences = new ArrayList<>();

        for (SearchModel searchModelRequest : searchModelsRequest) {
            SearchModel searchModel = searchModelRequest;
            searchModel.setReference(searchModelRequest.getReference());
            searchModel.setOrder(searchModelRequest.getOrder());

            // Save the SearchModel first to ensure it has an ID
            SearchModel savedSearchModel = searchModelRepository.save(searchModel);
            savedSearchModelReferences.add(savedSearchModel.getReference());

            // Check if places are provided in the request
            if (searchModelRequest.getPlaces() != null) {
                List<Place> places = searchModelRequest.getPlaces();

                for (Place place : places) {
                    // Fetch the Place by ID from the service
                    Place placeItem = placeServiceImpl.getById(place.getId());

                    if (placeItem == null) {
                        throw new AgencyException("Place not found for ID: " + place.getId());
                    }

                    // Link the Place with the SearchModel (by setting SearchModel ID)
                    placeItem.setSearchModel(savedSearchModel);  // Ensure the correct SearchModel is set
                    placeRepository.save(placeItem); // Save updated Place
                }
            } else {
                throw new AgencyException("No places provided for linking.");
            }
        }

        return savedSearchModelReferences;
    }

    // Read a SearchCircuit by ID (returns entity)
    public SearchModel getSearchCircuit(Integer id) {
        return searchModelRepository.findById(id)
                .orElseThrow(() -> new AgencyException(ERROR_CIRCUIT));
    }

    public SearchModel getSearchCircuitByRef(String reference) {
        return searchModelRepository.findByReference(reference);
    }



    @Transactional
    public void deleteSearchCircuit(Integer id) {
        searchModelRepository.findById(id).orElseThrow(() -> new AgencyException(ERROR_CIRCUIT));
        searchModelRepository.deleteById(id);
    }

    // Get all non-deleted SearchCircuits
    public List<SearchModel> getAllSearchCircuits() {
        return searchModelRepository.findAll();
    }

    // Check if a SearchCircuit exists and is not deleted

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupOldSearchCircuits() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(variableProperty.getOldCircuitDuration()); // Records older than 24 hours
        searchCircuitRepository.deleteRecordsOlderThan(expiryTime);
        logger.info("Old SearchCircuit records deleted at {} " , LocalDateTime.now());
    }
}
