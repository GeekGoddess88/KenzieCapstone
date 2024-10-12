package com.kenzie.appserver.controller;




import com.kenzie.appserver.service.IngredientService;
import com.kenzie.capstone.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<IngredientResponse>> addIngredient(@RequestBody IngredientCreateRequest ingredientCreateRequest) {
        try {
            return ingredientService.addIngredient(ingredientCreateRequest)
                    .thenApply(ingredientResponse -> ResponseEntity.status(HttpStatus.CREATED).body(ingredientResponse))
                    .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<IngredientResponse>> getIngredientById(@PathVariable String id) {
        return ingredientService.getIngredientById(id)
                .thenApply(ingredientResponse -> new ResponseEntity<>(ingredientResponse, HttpStatus.OK))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<IngredientResponse>>> getAllIngredients() throws IOException {
        return ingredientService.getAllIngredients()
                .thenApply(ingredientResponse -> new ResponseEntity<>(ingredientResponse, HttpStatus.OK))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<IngredientResponse>> updateIngredient(@PathVariable("id") String id, @RequestBody IngredientUpdateRequest ingredientUpdateRequest) throws IOException {
        return ingredientService.updateIngredient(id, ingredientUpdateRequest)
                .thenApply(ingredientResponse -> new ResponseEntity<>(ingredientResponse, HttpStatus.OK))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<DeleteIngredientResponse>> deleteIngredient(@PathVariable("id") String id) throws IOException {
        return ingredientService.deleteIngredient(id)
                .thenApply(deleteIngredientResponse -> new ResponseEntity<>(deleteIngredientResponse, HttpStatus.OK))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
