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


    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> addIngredient(@RequestBody IngredientCreateRequest ingredientCreateRequest) throws IOException {
        IngredientResponse response = ingredientService.addIngredient(ingredientCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredientById(@PathVariable String id) throws IOException {
        IngredientResponse response = ingredientService.getIngredientById(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getIngredients() throws IOException {
        List<IngredientResponse> responses = ingredientService.getAllIngredients();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable("id") String id, @RequestBody IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
        IngredientResponse response = ingredientService.updateIngredient(id, ingredientUpdateRequest);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteIngredientResponse> deleteIngredient(@PathVariable("id") String id) throws IOException {
        DeleteIngredientResponse response = ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
