package com.ouitrips.app.web.circuits;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.circuits.IStepsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/steps")
@AllArgsConstructor
public class StepsController {
    private final IStepsService stepsService;

    @PostMapping("/create")
    public ResponseEntity<?> createStep(
            @RequestParam(value = "mode_reference") Integer modeReference,
            @RequestParam(value = "circuit_reference") Integer circuitReference,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "location_reference", required = false) Integer locationReference,
            @RequestParam(value = "directions", required = false) String directions,
            @RequestParam(value = "order_step", required = false) Integer orderStep,
            @RequestParam(value = "state", required = false) Boolean state,
            @RequestParam(value = "duration", required = false) Float duration,
            @RequestParam(value = "distance", required = false) Float distance,
            @RequestParam(value = "location", required = false) String location
//            @RequestParam(value = "location_end_reference") Integer LocationEndReference,
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("directions", directions);
        params.put("orderStep", orderStep);
        params.put("state", state);
        params.put("duration", duration);
        params.put("distance", distance);
        params.put("circuitReference", circuitReference);
        params.put("locationReference", locationReference);
        params.put("location", location);
        params.put("modeReference", modeReference);

        return Response.responseData(stepsService.save(params));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStep(
            @RequestParam(value = "reference") Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "directions", required = false) String directions,
            @RequestParam(value = "order_step", required = false) Integer orderStep,
            @RequestParam(value = "state", required = false) Boolean state,
            @RequestParam(value = "duration", required = false) Float duration,
            @RequestParam(value = "distance", required = false) Float distance,
            @RequestParam(value = "circuit_reference", required = false) Integer circuitReference,
            @RequestParam(value = "location_reference", required = false) Integer locationReference,
            @RequestParam(value = "location", required = false) String location,
//            @RequestParam(value = "location_end_reference", required = false) Integer LocationEndReference,
            @RequestParam(value = "mode_reference", required = false) Integer modeReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("directions", directions);
        params.put("orderStep", orderStep);
        params.put("state", state);
        params.put("duration", duration);
        params.put("distance", distance);
        params.put("circuitReference", circuitReference);
        params.put("locationReference", locationReference);
        params.put("location", location);
        params.put("modeReference", modeReference);

        stepsService.save(params);
        return Response.updatedSuccessMessage();
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllSteps() {
        return Response.responseData(stepsService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getStep(@RequestParam Integer id) {
        return Response.responseData(stepsService.get(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStep(@RequestParam Integer id) {
        stepsService.delete(id);
        return Response.deletedSuccessMessage();
    }
    @GetMapping("/step_by_circuit")
    public ResponseEntity<?> getStepsByCircuit(@RequestParam Integer circuitReference) {
        return Response.responseData(stepsService.getStepByCircuit(circuitReference));
    }
}
