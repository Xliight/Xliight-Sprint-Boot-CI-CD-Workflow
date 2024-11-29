package com.ouitrips.app.web.circuits;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.circuits.ICircuitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/public/circuits")
@AllArgsConstructor
public class PublicCircuitController {
    private final ICircuitService circuitService;

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllCircuits() {
        return Response.responseData(circuitService.getAll());
    }

@PostMapping("/search")
public ResponseEntity<?> searchCircuits(@RequestParam(value = "query", required = false) String query,
                                        @RequestBody(required = false) Map<String, Map<String, Object>> variables) {
    if (variables == null) {
        return ResponseEntity.ok(new ArrayList<>());
    }
        return Response.responseData(circuitService.search(query,variables));
}

}
