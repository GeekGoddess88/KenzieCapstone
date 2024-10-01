package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.DrinkRepository;
import com.kenzie.appserver.repositories.model.DrinkRecord;
import com.kenzie.appserver.service.model.Drink;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DrinkService {
    private DrinkRepository drinkRepository;

    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public Drink addDrink(Drink drink) {
        validateAddDrink(drink);
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(drink.getId());
        drinkRecord.setName(drink.getName());
        drinkRecord.setIngredients(drink.getIngredients());
        drinkRecord.setRecipe(drink.getRecipe());
        drinkRepository.save(drinkRecord);
        return drink;
    }

    public Drink updateDrink(String id, Drink drink) {
//        validateUpdateDrink(drink);
        DrinkRecord drinkRecord = drinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drink with id " + id + " not found.", HttpStatus.NOT_FOUND));
        drinkRecord.setName(drink.getName());
        drinkRecord.setIngredients(drink.getIngredients());
        drinkRecord.setRecipe(drink.getRecipe());
        drinkRepository.save(drinkRecord);
        return drink;
    }

    public void deleteDrink(String id) {
        DrinkRecord drinkRecord = drinkRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Drink with id " + id + " not found.", HttpStatus.NOT_FOUND));
        drinkRepository.delete(drinkRecord);
    }

    public Drink findById(String id) {
        return drinkRepository
                .findById(id)
                .map(drink -> new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getRecipe()))
                .orElse(null);
    }

    public List<Drink> findAll() {
        List<Drink> drinks = new ArrayList<>();
        drinkRepository
                .findAll()
                .forEach(drink -> drinks.add(new Drink(drink.getId(), drink.getName(), drink.getIngredients(), drink.getRecipe())));
        return drinks;
    }

    // Convert string to arrayList helper method
    private List<String> convertStringToList(String string) {
        return Arrays.asList(string.split(","));
    }


    // Validation helper methods
    private void validateAddDrink(Drink drink) {
        if (drink.getName() == null || drink.getName().isEmpty()) {
            throw new ValidationException("Drink name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (drink.getIngredients() == null || drink.getIngredients().isEmpty()) {
            throw new ValidationException("Drink must have assigned ingredients", HttpStatus.BAD_REQUEST);
        }
        if (drink.getRecipe() == null | drink.getRecipe().isEmpty()) {
            throw new ValidationException("Drink must include a recipe", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateUpdateDrink(Drink drink) {
        if (drink.getName() == null || drink.getName().isEmpty()) {
            throw new ValidationException("Drink name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (drink.getIngredients() == null || drink.getIngredients().isEmpty()) {
            throw new ValidationException("Drink must have assigned ingredients", HttpStatus.BAD_REQUEST);
        }
        if (drink.getRecipe() == null | drink.getRecipe().isEmpty()) {
            throw new ValidationException("Drink must include a recipe", HttpStatus.BAD_REQUEST);
        }
    }
}
