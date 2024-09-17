package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DrinkDAO;
import com.kenzie.appserver.service.DrinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drinks")
public class IngredientController {

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkDAO> getDrinkById(@PathVariable String id) {
        try {
            DrinkDAO drink = drinkService.findById(id);
        }
    }
}
