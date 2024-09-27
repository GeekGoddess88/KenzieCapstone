package com.kenzie.appserver.controller;


import com.kenzie.capstone.service.model.*;
import com.kenzie.appserver.service.DrinkService;

import org.springframework.boot.context.properties.bind.Name;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;


    public DrinkController(@Name("DrinkService") DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @PostMapping
    public ResponseEntity<DrinkResponse> addDrink(@RequestBody DrinkCreateRequest drinkCreateRequest) throws IOException {
        DrinkResponse response = drinkService.addDrink(drinkCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> getDrinkById(@PathVariable("id") String id) throws IOException {
        DrinkResponse response = drinkService.getDrinkById(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DrinkResponse>> getDrinks() throws IOException {
        List<DrinkResponse> responses = drinkService.getAllDrinks();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable("id") String id, @RequestBody DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        DrinkResponse response = drinkService.updateDrink(id, drinkUpdateRequest);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteDrinkResponse> deleteDrink(@PathVariable("id") String id) throws IOException {
        DeleteDrinkResponse response = drinkService.deleteDrinkById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
