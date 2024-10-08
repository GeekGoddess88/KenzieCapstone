package com.kenzie.appserver.controller;

import dagger.internal.DaggerCollections;
import net.bytebuddy.agent.builder.LambdaFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/")
    public ResponseEntity<String> getServiceName() {
        return ResponseEntity.ok("Service is up and running");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("Application version 1.0.0");
    }
}
