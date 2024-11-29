package com.ouitrips.app.web.circuits;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.services.circuits.ICircuitService;
import com.ouitrips.app.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/circuits")
@AllArgsConstructor
public class CircuitController {
    private final ICircuitService circuitService;
    private final UserUtils userUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createCircuit(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "distance", required = false) Double distance,
            @RequestParam(value = "circuit_group_reference", required = false) Integer circuitGroupReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("distance", distance);
        params.put("circuitGroupReference", circuitGroupReference);

        return Response.responseData(circuitService.save(params));
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateCircuit(
            @RequestParam(value = "reference") Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "distance", required = false) Double distance,
            @RequestParam(value = "circuit_group_reference", required = false) Integer circuitGroupReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("reference", id);
        params.put("name", name);
        params.put("distance", distance);
        params.put("circuitGroupReference", circuitGroupReference);

        circuitService.save(params);
        return Response.updatedSuccessMessage();
    }
    @GetMapping("/my_circuits")
    public ResponseEntity<?> getAllByUser() {
        User user=userUtils.userAuthenticated();
        return Response.responseData(circuitService.getAllByUser());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCircuit(@RequestParam Integer id) {
        return Response.responseData(circuitService.get(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCircuit(@RequestParam Integer id) {
        circuitService.delete(id);
        return Response.deletedSuccessMessage();
    }
    @GetMapping("/live")
    public ResponseEntity<?> getLiveCircuits() {
        return Response.responseData(circuitService.getLiveCircuits());
    }
}
