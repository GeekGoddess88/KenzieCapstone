package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.IngredientCreateRequest;
import com.kenzie.appserver.controller.model.IngredientResponse;
import com.kenzie.appserver.controller.model.IngredientUpdateRequest;
import com.kenzie.appserver.repositories.IngredientRepository;
import com.kenzie.appserver.service.IngredientService;
import com.kenzie.appserver.service.model.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private IngredientRepository ingredientRepository;
    private IngredientService ingredientService;

    IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredient(@PathVariable("id") String id) {
        Ingredient ingredient = ingredientService.findById(id);
        if (ingredient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ingredientToResponse(ingredient));
    }

    @GetMapping("/all")
    public ResponseEntity<List<IngredientResponse>> getIngredient() {
        List<Ingredient> ingredients = ingredientService.findAll();
        List<IngredientResponse> ingredientResponses = ingredients.stream()
                .map(ingredient -> ingredientToResponse(ingredient)).collect(Collectors.toList());
        return ResponseEntity.ok(ingredientResponses);
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> addIngredient(@RequestBody IngredientCreateRequest ingredientCreateRequest) {
        Ingredient ingredient = new Ingredient(randomUUID().toString(),
                ingredientCreateRequest.getName(),
                ingredientCreateRequest.getQuantity());
        ingredientService.addIngredient(ingredient);

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setId(ingredient.getId());
        ingredientResponse.setName(ingredient.getName());
        ingredientResponse.setQuantity(ingredient.getQuantity());

        return ResponseEntity.created(URI.create("/ingredient/" + ingredientResponse.getId())).body(ingredientResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable("id") String id, @RequestBody IngredientUpdateRequest ingredientUpdateRequest) {
        Ingredient ingredientRecord = ingredientService.findById(id);
//                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id " + id + " not found.", HttpStatus.NOT_FOUND));
        Ingredient ingredientUpdate = new Ingredient(ingredientRecord.getId(),
                ingredientUpdateRequest.getName(),
                ingredientUpdateRequest.getQuantity());
        ingredientService.updateIngredient(id, ingredientUpdate);

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setId(ingredientUpdate.getId());
        ingredientResponse.setName(ingredientUpdate.getName());
        ingredientResponse.setQuantity(ingredientUpdate.getQuantity());

        return ResponseEntity.ok(ingredientResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable("id") String id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.ok("Ingredient removed.");
    }

    // Convert IngredientRecord to IngredientResponse helper method
    private IngredientResponse ingredientToResponse(Ingredient ingredient) {
        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setId(ingredient.getId());
        ingredientResponse.setName(ingredient.getName());
        ingredientResponse.setQuantity(ingredient.getQuantity());
        return ingredientResponse;
    }
}
