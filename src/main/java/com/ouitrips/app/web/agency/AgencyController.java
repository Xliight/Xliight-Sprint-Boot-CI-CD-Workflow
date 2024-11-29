package com.ouitrips.app.web.agency;

import com.ouitrips.app.dtos.agencydto.AgencyDTO;
import com.ouitrips.app.entities.agency.Agency;
import com.ouitrips.app.services.agency.IAgencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${REST_NAME}/agencies")
@AllArgsConstructor
public class AgencyController {

    private final IAgencyService agencyService;



    @PostMapping
    public ResponseEntity<AgencyDTO> createAgency(@RequestBody Agency agencyDTO) {
        AgencyDTO createdAgency = agencyService.createAgency(agencyDTO);
        return ResponseEntity.ok(createdAgency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgencyDTO> updateAgency(@PathVariable Integer id, @RequestBody Agency agencyDTO) {
        AgencyDTO updatedAgency = agencyService.updateAgency(id, agencyDTO);
        return ResponseEntity.ok(updatedAgency);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Integer id) {
        agencyService.deleteAgency(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable Integer id) {
        AgencyDTO agency = agencyService.getAgencyById(id);
        return ResponseEntity.ok(agency);
    }
    @GetMapping
    public ResponseEntity<List<AgencyDTO>> getAllAgencies() {
        List<AgencyDTO> agencies = agencyService.getAllAgencies();
        return ResponseEntity.ok(agencies);
    }


}