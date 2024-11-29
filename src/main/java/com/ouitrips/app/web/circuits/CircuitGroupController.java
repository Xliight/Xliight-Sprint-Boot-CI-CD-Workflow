package com.ouitrips.app.web.circuits;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.circuits.ICircuitGroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/circuit_groups")
@AllArgsConstructor
public class CircuitGroupController {
    private final ICircuitGroupService circuitGroupService;

    @PostMapping("/create")
    public ResponseEntity<?> createCircuitGroup(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "user_reference", required = false) Integer userReference,
            @RequestParam(value = "category_reference", required = false) Integer categoryReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("color", color);
        params.put("icon", icon);
        params.put("userReference", userReference);
        params.put("categoryReference", categoryReference);

        return Response.responseData(circuitGroupService.save(params));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCircuitGroup(
            @RequestParam(value = "reference") Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "user_reference", required = false) Integer userReference,
            @RequestParam(value = "category_reference", required = false) Integer categoryReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("description", description);
        params.put("color", color);
        params.put("icon", icon);
        params.put("userReference", userReference);
        params.put("categoryReference", categoryReference);

        circuitGroupService.save(params);
        return Response.updatedSuccessMessage();
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllCircuitGroups() {
        return Response.responseData(circuitGroupService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCircuitGroup(@RequestParam Integer id) {
        return Response.responseData(circuitGroupService.get(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCircuitGroup(@RequestParam Integer id) {
        circuitGroupService.delete(id);
        return Response.deletedSuccessMessage();
    }
}
