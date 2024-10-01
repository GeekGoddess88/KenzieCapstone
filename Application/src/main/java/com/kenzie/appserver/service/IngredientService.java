package com.kenzie.appserver.service;

import com.amazonaws.services.cloudwatch.model.ResourceNotFoundException;
import com.kenzie.appserver.repositories.IngredientRepository;
import com.kenzie.appserver.repositories.model.IngredientRecord;
import com.kenzie.appserver.service.model.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientService {
    private IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        if (ingredient.getName() == "" || ingredient.getName() == null) {
            throw new ValidationException("Ingredient name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (ingredient.getQuantity() < 0) {
            throw new ValidationException("Ingredient quantity cannot be less than zero", HttpStatus.BAD_REQUEST);
        }
        IngredientRecord ingredientRecord = new IngredientRecord();
        ingredientRecord.setId(ingredient.getId());
        ingredientRecord.setName(ingredient.getName());
        ingredientRecord.setQuantity(ingredient.getQuantity());
        ingredientRepository.save(ingredientRecord);
        return ingredient;
    }

    public Ingredient updateIngredient(String id, Ingredient ingredient) {
        IngredientRecord ingredientRecord = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id " + id + " not found."));
        ingredientRecord.setName(ingredient.getName());
        ingredientRecord.setQuantity(ingredient.getQuantity());
        ingredientRepository.save(ingredientRecord);
        return ingredient;
    }

    public void deleteIngredient(String id) {
        ingredientRepository.deleteById(id);
    }

    public Ingredient findById(String id) {
        return ingredientRepository
                .findById(id)
                .map(ingredient -> new Ingredient(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getQuantity()))
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id " + id + " not found."));
    }

    public List<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository
                .findAll()
                .forEach(ingredient -> ingredients.add(new Ingredient(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getQuantity())));
        return ingredients;
    }
}
