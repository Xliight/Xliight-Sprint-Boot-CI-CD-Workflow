package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.LinksLs;
import com.ouitrips.app.entities.circuits.Location;
import com.ouitrips.app.entities.circuits.SpecificPlace;
import com.ouitrips.app.entities.circuits.Step;
import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.LocationsMapper;
import com.ouitrips.app.repositories.security.circuits.LinksLsRepository;
import com.ouitrips.app.repositories.security.circuits.LocationsRepository;
import com.ouitrips.app.repositories.security.circuits.SpecificPlaceRepository;
import com.ouitrips.app.repositories.security.circuits.StepsRepository;
import com.ouitrips.app.repositories.security.places.PlaceRepository;
import com.ouitrips.app.services.circuits.ILocationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class LocationServiceImp implements ILocationService {
    private final LocationsRepository locationsRepository;
    private final LocationsMapper locationsMapper;
    private final PlaceRepository placeRepository;
    private final LinksLsRepository linksLsRepository;
    private final StepsRepository stepsRepository;
    private final SpecificPlaceRepository specificPlaceRepository;
    @Override
    public Object save(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        String description = (String) params.get("description");
        Double latitude = (Double) params.get("latitude");
        Double longitude = (Double) params.get("longitude");
        String location = (String) params.get("location");
        String name = (String) params.get("name");
        Integer rating = (Integer) params.get("rating");
        Integer placeReference = (Integer) params.get("placeReference");
        Integer placeSpecReference = (Integer) params.get("placeSpecReference");

        Location loc;
        if (id == null) {
            loc = new Location();
        } else {
            loc = this.getById(id);
        }

        if (description != null){
            loc.setDescription(description);
        }
        if (latitude != null){
            loc.setLatitude(latitude);
        }
        if (longitude != null){
            loc.setLongitude(longitude);
        }
        if (location != null){
            loc.setAddress(location);
        }
        if (name != null){
            loc.setName(name);
        }
        if (rating != null){
            loc.setRating(rating);
        }
        if (placeReference != null) {
            Place place = placeRepository.findById(placeReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Place with ID " + placeReference + " not found"));
            loc.setPlace(place);
        }
        if (placeSpecReference != null) {
            SpecificPlace specificPlace = specificPlaceRepository.findById(placeSpecReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("specificPlace with ID " + placeSpecReference + " not found"));
            loc.setSpecificPlace(specificPlace);
        }

        return Map.of("reference", locationsRepository.save(loc).getId());
    }
    @Override
    @Transactional
    public List<Object> getLocationByStep(Integer stepId) {
        Step step = stepsRepository.findById(stepId)
                .orElseThrow(() -> new ExceptionControllerAdvice.ObjectNotFoundException("Step not found"));

        List<LinksLs> linksLs = linksLsRepository.findByStep(step);
        List<Object> listLocations = new ArrayList<>();
        if(!linksLs.isEmpty()){
            listLocations = List.of(
                    locationsMapper.apply(linksLs.get(0).getLocationStart()),
                    locationsMapper.apply(linksLs.get(0).getLocationEnd())
            );
        }
        return listLocations;
    }
    @Override
    public List<Object> getAll() {
        return locationsRepository.findAll().stream().map(locationsMapper).toList();
    }
    @Override
    public Object get(Integer id) {
        return locationsMapper.apply(this.getById(id));
    }

    public Location getById(Integer id) {
        return locationsRepository.findById(id).orElseThrow(
                () -> new ExceptionControllerAdvice.ObjectNotFoundException("Location not found")
        );
    }
    @Override
    public void delete(Integer id) {
        locationsRepository.delete(this.getById(id));
    }
}
