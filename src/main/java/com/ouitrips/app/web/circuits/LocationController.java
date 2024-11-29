package com.ouitrips.app.web.circuits;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.circuits.ILocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("${REST_NAME}/locations")
@AllArgsConstructor
public class LocationController {
    private final ILocationService locationService;

    @PostMapping("/create")
    public ResponseEntity<?> createLocation(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "place_reference", required = false) Integer placeReference,
            @RequestParam(value = "place_spec_reference", required = false) Integer placeSpecReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("location", location);
        params.put("name", name);
        params.put("rating", rating);
        params.put("placeReference", placeReference);
        params.put("placeSpecReference", placeSpecReference);

        return Response.responseData(locationService.save(params));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLocation(
            @RequestParam(value = "reference") Integer id,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "place_reference", required = false) Integer placeReference,
            @RequestParam(value = "place_spec_reference", required = false) Integer placeSpecReference
            ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("description", description);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("location", location);
        params.put("name", name);
        params.put("rating", rating);
        params.put("placeReference", placeReference);
        params.put("placeSpecReference", placeSpecReference);

        locationService.save(params);
        return Response.updatedSuccessMessage();
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllLocations() {
        return Response.responseData(locationService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getLocation(@RequestParam Integer id) {
        return Response.responseData(locationService.get(id));
    }
    @GetMapping("/get_by_step")
    public ResponseEntity<?> getLocationByStep(@RequestParam Integer stepId) {
        return Response.responseData(locationService.getLocationByStep(stepId));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLocation(@RequestParam Integer id) {
        locationService.delete(id);
        return Response.deletedSuccessMessage();
    }
}
