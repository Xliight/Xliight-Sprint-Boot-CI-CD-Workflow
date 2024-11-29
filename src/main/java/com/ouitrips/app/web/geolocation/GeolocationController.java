package com.ouitrips.app.web.geolocation;

import com.ouitrips.app.dtos.geolocation.ResponseAPIDTO;
import com.ouitrips.app.entities.geolocation.ResponseAPI;
import com.ouitrips.app.services.geolocation.IFetchDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${REST_NAME}/geolocation")
@AllArgsConstructor
public class GeolocationController {

    private final IFetchDataService responseAPIService;

    // Endpoint to get all responses
    @GetMapping
    public ResponseEntity<List<ResponseAPIDTO>> getAllResponses() {
        List<ResponseAPIDTO> responses = responseAPIService.getAllResponseDTOs();
        return ResponseEntity.ok(responses);
    }

    // Endpoint to get a response by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO> getResponseById(@PathVariable Integer id) {
        return responseAPIService.getResponseById(id)
                .map(responseAPIService::convertToDTO) // Convert to DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new response
    @PostMapping
    public ResponseEntity<ResponseAPI> createResponse(@RequestBody ResponseAPI responseAPI) {
        ResponseAPI createdResponse = responseAPIService.createResponse(responseAPI);
        return ResponseEntity.status(201).body(createdResponse);
    }

    // Endpoint to update an existing response
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI> updateResponse(@PathVariable Integer id, @RequestBody ResponseAPI responseAPI) {
        ResponseAPI updatedResponse = responseAPIService.updateResponse(id, responseAPI);
        return updatedResponse != null
                ? ResponseEntity.ok(updatedResponse)
                : ResponseEntity.notFound().build();
    }

    // Endpoint to delete a response by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Integer id) {
        responseAPIService.deleteResponse(id);
        return ResponseEntity.noContent().build();
    }


}
