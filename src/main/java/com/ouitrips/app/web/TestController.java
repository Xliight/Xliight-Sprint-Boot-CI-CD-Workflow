package com.ouitrips.app.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${REST_NAME}/test")
@AllArgsConstructor
public class TestController {
    @GetMapping
    public ResponseEntity<?> getAllAppointments(){
        return ResponseEntity.ok("Hello this is a test rest");
    }
}
