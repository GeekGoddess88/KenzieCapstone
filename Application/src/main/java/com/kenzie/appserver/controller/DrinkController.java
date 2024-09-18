package com.kenzie.appserver.controller;

import com.kenzie.appserver.service.DrinkService;
import com.kenzie.capstone.service.model.DrinkRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Inject
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkRecord> findDrinkById(@PathVariable String id) {
        DrinkRecord drink = drinkService.findById(id);
        if (drink == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(drink);
    }

    @PostMapping
    public ResponseEntity<Void> saveDrink(@RequestBody DrinkRecord drink) {
        drinkService.saveDrink(drink);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<Void> updateDrink(@PathVariable String id, @RequestBody DrinkRecord drink) {
        drink.setId(id);
        drinkService.updateDrink(id, drink);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDrink(@PathVariable String id) {
        drinkService.deleteDrink(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<DrinkRecord>> findAllDrinks() {
        return ResponseEntity.ok(drinkService.findAllDrinks());
    }
}
