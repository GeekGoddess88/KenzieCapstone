package com.kenzie.appserver.controller;

import com.kenzie.capstone.service.LambdaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return new ResponseEntity<>("Application version 1.0.0", HttpStatus.OK);
    }
}
