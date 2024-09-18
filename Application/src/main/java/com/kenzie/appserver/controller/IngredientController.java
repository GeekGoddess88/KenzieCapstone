package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DrinkDAO;
import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.IngredientService;
import com.kenzie.appserver.service.model.Ingredient;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Inject
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientRecord> getIngredientById(@PathVariable String id) {
        IngredientRecord ingredientRecord = ingredientService.findById(id);
        if (ingredientRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(ingredientRecord);
    }

    @PostMapping
    public ResponseEntity<Void> createIngredient(@RequestBody IngredientRecord ingredient) {
        ingredientService.save(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateIngredient(@PathVariable String id, @RequestBody IngredientRecord ingredient) {
        ingredient.setId(id);
        ingredientService.update(id, ingredient);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable String id) {
        ingredientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<IngredientRecord>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.findAll());
    }
}
