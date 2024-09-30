package com.kenzie.appserver.controller;




import com.kenzie.appserver.service.IngredientService;
import com.kenzie.capstone.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> addIngredient(@RequestBody IngredientCreateRequest ingredientCreateRequest) {
        try {
            IngredientResponse ingredientResponse = ingredientService.addIngredient(ingredientCreateRequest);
            return new ResponseEntity<>(ingredientResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredientById(@PathVariable String id) {
        try {
            IngredientResponse ingredientResponse = ingredientService.getIngredientById(id);
            if (ingredientResponse != null) {
                return new ResponseEntity<>(ingredientResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        try {
            List<IngredientResponse> ingredientResponseList = ingredientService.getAllIngredients();
            return new ResponseEntity<>(ingredientResponseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable("id") String id, @RequestBody IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
        try {
            IngredientResponse updatedIngredientResponse = ingredientService.updateIngredient(id, ingredientUpdateRequest);
            return new ResponseEntity<>(updatedIngredientResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteIngredientResponse> deleteIngredient(@PathVariable("id") String id) throws IOException {
        try {
            DeleteIngredientResponse deleteIngredientResponse = ingredientService.deleteIngredient(id);
            return new ResponseEntity<>(deleteIngredientResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
