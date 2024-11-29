package com.ouitrips.app.web.system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/status")
public class StatusController {
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Object> getStatusSystem() {
        return ResponseEntity.ok(
                Map.of(
                        "status", "active",
                        "version", "1.6.1",
                        "timestamp", System.currentTimeMillis()
                )
        );
    }
    
}
