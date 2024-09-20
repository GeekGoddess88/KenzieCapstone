package com.kenzie.appserver.controller;


import com.kenzie.appserver.service.IngredientService;
import com.kenzie.capstone.service.model.*;
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

    @PostMapping
    public ResponseEntity<IngredientResponse> addIngredient(@RequestBody IngredientCreateRequest ingredientCreateRequest) {
        IngredientResponse response = ingredientService.addIngredient(ingredientCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredientById(@PathVariable String id) {
        IngredientResponse response = ingredientService.getIngredientById(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getIngredients() {
        List<IngredientResponse> responses = ingredientService.getAllIngredients();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable String id, @RequestBody IngredientUpdateRequest ingredientUpdateRequest) {
        IngredientResponse response = ingredientService.updateIngredient(id, ingredientUpdateRequest);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteIngredientResponse> deleteIngredient(@PathVariable String id) {
        DeleteIngredientResponse response = ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
