package com.kenzie.appserver.controller;


import com.kenzie.capstone.service.model.*;
import com.kenzie.appserver.service.DrinkService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<DrinkResponse>> addDrink(@RequestBody DrinkCreateRequest drinkCreateRequest) throws IOException {
        return drinkService.addDrink(drinkCreateRequest)
                .thenApply(drinkResponse -> new ResponseEntity<>(drinkResponse, HttpStatus.CREATED))
                .exceptionally(ex -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<DrinkResponse>> getDrinkById(@PathVariable String id) throws IOException {
        return drinkService.getDrinkById(id)
                .thenApply(drinkResponse -> new ResponseEntity<>(drinkResponse, HttpStatus.OK))
                .exceptionally(ex -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<DrinkResponse>>> getAllDrinks() throws IOException {
            return drinkService.getAllDrinks()
                    .thenApply(drinkResponse -> new ResponseEntity<>(drinkResponse, HttpStatus.OK))
                    .exceptionally(ex -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<DrinkResponse>> updateDrink(@PathVariable String id, @RequestBody DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        return drinkService.updateDrink(id, drinkUpdateRequest)
                .thenApply(drinkResponse -> new ResponseEntity<>(drinkResponse, HttpStatus.OK))
                .exceptionally(ex -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<DeleteDrinkResponse>> deleteDrinkById(@PathVariable String id) throws IOException {
        return drinkService.deleteDrinkById(id)
                .thenApply(drinkResponse -> new ResponseEntity<>(drinkResponse, HttpStatus.OK))
                .exceptionally(ex -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
