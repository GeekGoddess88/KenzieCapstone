package com.kenzie.appserver.controller;

import com.kenzie.appserver.service.DrinkService;
import com.kenzie.capstone.service.model.DrinkRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkRecord> getDrinkById(@PathVariable String id) {
        DrinkRecord drink = drinkService.findById(id);
        return drink != null ? ResponseEntity.ok(drink) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveDrink(@RequestBody DrinkRecord drink) {
        drinkService.saveDrink(drink);
        return ResponseEntity.ok("Drink saved");
    }

    @PostMapping
    public ResponseEntity<String> updateDrink(@RequestBody String id, DrinkRecord drink) {
        drinkService.updateDrink(id, drink);
        return ResponseEntity.ok("Drink updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDrink(@PathVariable String id) {
        drinkService.deleteDrink(id);
        return ResponseEntity.ok("Drink deleted");
    }

    @GetMapping("/drinks")
    public ResponseEntity<List<DrinkRecord>> findAllDrinks() {
        List<DrinkRecord> drinks = drinkService.findAllDrinks();
        return ResponseEntity.ok(drinks);
    }
}
