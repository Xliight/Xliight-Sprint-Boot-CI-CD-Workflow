package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.CircuitMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitGroupRepository;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.services.circuits.ICircuitService;
import com.ouitrips.app.services.places.impl.PlaceServiceImpl;
import com.ouitrips.app.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@AllArgsConstructor
@Service
public class CircuitServiceImpl implements ICircuitService {

    private final CircuitRepository circuitRepository;
    private final CircuitMapper circuitMapper;
    private final CircuitGroupRepository circuitGroupRepository;
    private final UserUtils userUtils;
    private final CircuitGroupServiceImpl circuitGroupService;
    private final PlaceServiceImpl placeService;
    private static final String EXCLUDED_PLACE_TYPE_KEY = "excludedPlaceType";
    private static final String INCLUDE_PLACE_TYPE_KEY = "includePlaceType";
    @Override
    public Object save(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        String name = (String) params.get("name");
        Double distance = (Double) params.get("distance");
        Integer circuitGroupReference = (Integer) params.get("circuitGroupReference");
        User userConnected = userUtils.userAuthenticated();
        Circuit circuit;
        if (id == null) {
            circuit = new Circuit();
            circuit.setIsDeleted(false);
        } else {
            circuit = this.getById(id);
        }
        if (name != null){
            circuit.setName(name);
        }
        if (distance != null){
            circuit.setDistance(distance);
        }
        CircuitGroup circuitGroup = null;
        if (circuitGroupReference != null) {
            circuitGroup = circuitGroupRepository.findById(circuitGroupReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("CircuitGroup with ID " + circuitGroupReference + " not found"));
            circuit.setCircuitGroup(circuitGroup);
        }else {
            List<CircuitGroup> listDefaultCircuitGroup = circuitGroupRepository.findByIsDefaultAndUser(true, userConnected);
            if(listDefaultCircuitGroup.isEmpty()){
                circuitGroup = circuitGroupService.addDefaultByUser(userConnected);
            }else{
                circuitGroup = listDefaultCircuitGroup.get(0);
            }
        }
        circuit.setCircuitGroup(circuitGroup);
        return Map.of("reference", circuitRepository.save(circuit).getId());
    }
    @Override
    public Object getAllByUser() {
        User userConnected = userUtils.userAuthenticated();
        //Check role
        return circuitRepository.getAllByUser(userConnected, false)
                .stream()
                .map(circuitMapper)
                .toList();
    }
    @Override
    @Transactional
    public Object getAll() {
        return circuitRepository.findByIsDeleted(false).stream().map(circuitMapper::applyDetail).toList();
    }
    @Override
    public Object get(Integer id) {
        Circuit circuit = this.getById(id);
        if(!Boolean.TRUE.equals(circuit.getIsDeleted())){
            return circuitMapper.apply(circuit);
        }
        throw new ExceptionControllerAdvice.GeneralException("Circuit not found");
    }
    public Circuit getById(Integer id) {
        return circuitRepository.findById(id).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("Circuit not found")
        );
    }
    @Override
    public void delete(Integer id) {
        Circuit circuit = this.getById(id);
        circuit.setIsDeleted(true);
        circuitRepository.save(circuit);
    }
    @Override
    @Transactional
    public Object getLiveCircuits() {
        return circuitRepository.findLiveCircuits().stream().map(circuitMapper).toList();
    }
    @Override
    @Transactional
    public Object search(String query, Map<String, Map<String, Object>> variables) {

        log.info("Query: {}", query);
        log.info("Variables: {}", variables);
        Map<String, Object> searchParams = variables != null ? variables.get("search") : null;
    Map<String, Object> filterParams = variables != null ? variables.get("filter") : null;
    Map<String, Object> optionsParams = variables != null ? variables.get("options") : null;
    Map<String, Object> params = new HashMap<>();

    if (searchParams != null) {
        Map<String, Object> itinerary = (Map<String, Object>) searchParams.get("itinerary");
        Map<String, Object> passengers = (Map<String, Object>) searchParams.get("passengers");
        Map<String, Object> transportMods = (Map<String, Object>) searchParams.get("transport_mods");

        log.info("Itinerary: {}", itinerary);
        log.info("Passengers: {}", passengers);
        log.info("Transport Mods: {}", transportMods);

        if (itinerary != null) {
            Map<String, Object> source = (Map<String, Object>) itinerary.get("source");
            Map<String, Object> visitedLocations = (Map<String, Object>) itinerary.get("visited_locations");
            Map<String, Object> destination = (Map<String, Object>) itinerary.get("destination");
            Map<String, Object> outboundDepartureDate = (Map<String, Object>) itinerary.get("outboundDepartureDate");
            Map<String, Object> inboundDepartureDate = (Map<String, Object>) itinerary.get("inboundDepartureDate");

            log.info("Source: {}", source);
            log.info("Visited Locations: {}", visitedLocations);
            log.info("Destination: {}", destination);
            log.info("Outbound Departure Date: {}", outboundDepartureDate);
            log.info("Inbound Departure Date: {}", inboundDepartureDate);

            if (source != null) {
                List<String> sourceCities = (List<String>) source.get("ids");
                params.put("sourceCities", sourceCities);
                log.info("Source Cities: {}", sourceCities);
            }
            if (visitedLocations != null) {
                List<String> visitedCities = (List<String>) visitedLocations.get("ids");
                params.put("visitedCities", visitedCities);
                log.info("Visited Cities: {}", visitedCities);
            }
            if (destination != null) {
                List<String> destinationCities = (List<String>) destination.get("ids");
                params.put("destinationCities", destinationCities);
                log.info("Destination Cities: {}", destinationCities);
            }

            if (outboundDepartureDate != null) {
                String start = (String) outboundDepartureDate.get("start");
                String end = (String) outboundDepartureDate.get("end");
                params.put("outboundDepartureDateStart", start);
                params.put("outboundDepartureDateEnd", end);
                log.info("Outbound Departure Start: {}", start);
                log.info("Outbound Departure End: {}", end);
            }
            if (inboundDepartureDate != null) {
                String start = (String) inboundDepartureDate.get("start");
                String end = (String) inboundDepartureDate.get("end");
                params.put("inboundDepartureDateStart", start);
                params.put("inboundDepartureDateEnd", end);
                log.info("Inbound Departure Start: {}", start);
                log.info("Inbound Departure End: {}", end);
            }
        }

        if (passengers != null) {
            int adults = (int) passengers.getOrDefault("adults", 0);
            int children = (int) passengers.getOrDefault("children", 0);
            int infants = (int) passengers.getOrDefault("infants", 0);
            params.put("adults", adults);
            params.put("children", children);
            params.put("infants", infants);
            log.info("Adults: {}", adults);
            log.info("Children: {}", children);
            log.info("Infants: {}", infants);
        }

        if (transportMods != null) {
            boolean fly = (boolean) transportMods.getOrDefault("fly", false);
            boolean train = (boolean) transportMods.getOrDefault("train", false);
            boolean drive = (boolean) transportMods.getOrDefault("drive", false);
            boolean motorcycle = (boolean) transportMods.getOrDefault("MotoCycle", false);
            boolean bicycle = (boolean) transportMods.getOrDefault("Bicycle", false);
            boolean walk = (boolean) transportMods.getOrDefault("Walk", false);
            params.put("fly", fly);
            params.put("train", train);
            params.put("drive", drive);
            params.put("motorcycle", motorcycle);
            params.put("bicycle", bicycle);
            params.put("walk", walk);
            log.info("Fly: {}", fly);
            log.info("Train: {}", train);
            log.info("Drive: {}", drive);
            log.info("Motorcycle: {}", motorcycle);
            log.info("Bicycle: {}", bicycle);
            log.info("Walk: {}", walk);
        }
    }

        if (filterParams != null) {
            if (filterParams.containsKey(EXCLUDED_PLACE_TYPE_KEY)) {
                List<String> excludedPlaceType = (List<String>) filterParams.get(EXCLUDED_PLACE_TYPE_KEY);
                params.put(EXCLUDED_PLACE_TYPE_KEY, excludedPlaceType);
                log.info("Excluded Place Type Codes: {}", excludedPlaceType);
            }
            if (filterParams.containsKey(INCLUDE_PLACE_TYPE_KEY)) {
                List<String> includePlaceType = (List<String>) filterParams.get(INCLUDE_PLACE_TYPE_KEY);
                params.put(INCLUDE_PLACE_TYPE_KEY, includePlaceType);
                log.info("Include Place Type Codes: {}", includePlaceType);
            }
        }
    return placeService.searchTestPlace(params);
    }
}




