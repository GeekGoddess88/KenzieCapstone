package com.kenzie.appserver.controller;

import com.kenzie.capstone.service.model.*;
import com.kenzie.appserver.service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @PostMapping
    public ResponseEntity<DrinkResponse> addDrink(@RequestBody DrinkCreateRequest drinkCreateRequest) {
        DrinkResponse response = drinkService.addDrink(drinkCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkResponse> getDrinkById(@PathVariable("id") String id) {
        DrinkResponse response = drinkService.getDrinkById(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DrinkResponse>> getDrinks() {
        List<DrinkResponse> responses = drinkService.getAllDrinks();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable("id") String id, @RequestBody DrinkUpdateRequest drinkUpdateRequest) {
        DrinkResponse response = drinkService.updateDrink(id, drinkUpdateRequest);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteDrinkResponse> deleteDrink(@PathVariable("id") String id) {
        DeleteDrinkResponse response = drinkService.deleteDrinkById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
