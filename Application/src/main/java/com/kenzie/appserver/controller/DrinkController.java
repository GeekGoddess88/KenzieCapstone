package com.kenzie.appserver.controller;


import com.kenzie.capstone.service.model.*;
import com.kenzie.appserver.service.DrinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(@Name("DrinkService") DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @PostMapping
    public ResponseEntity<DrinkResponse> addDrink(@RequestBody DrinkCreateRequest drinkCreateRequest) throws IOException {
        try {
            DrinkResponse drinkResponse = drinkService.addDrink(drinkCreateRequest);
            return new ResponseEntity<>(drinkResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> getDrinkById(@PathVariable String id) throws IOException {
        try {
            DrinkResponse drinkResponse = drinkService.getDrinkById(id);
            return new ResponseEntity<>(drinkResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<DrinkResponse>> getAllDrinks() throws IOException {
        try {
            List<DrinkResponse> drinkResponses = drinkService.getAllDrinks();
            return new ResponseEntity<>(drinkResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable String id, @RequestBody DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        try {
            DrinkResponse drinkResponse = drinkService.updateDrink(id, drinkUpdateRequest);
            return new ResponseEntity<>(drinkResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteDrinkResponse> deleteDrinkById(@PathVariable String id) throws IOException {
        try {
            DeleteDrinkResponse deleteDrinkResponse = drinkService.deleteDrinkById(id);
            return new ResponseEntity<>(deleteDrinkResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
