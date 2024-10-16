package com.kenzie.appserver;



import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.IngredientService;

import com.kenzie.capstone.service.model.DrinkCreateRequest;
import com.kenzie.capstone.service.model.IngredientCreateRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Component
public class ApplicationStartUpListener implements ApplicationListener<ApplicationReadyEvent> {

    private final DrinkService drinkService;
    private final IngredientService ingredientService;

    @Autowired
    public ApplicationStartUpListener(DrinkService drinkService, IngredientService ingredientService) {
        this.drinkService = drinkService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        List<IngredientCreateRequest> ingredients = Arrays.asList(
                new IngredientCreateRequest(UUID.randomUUID().toString(), "Water", 500),
                new IngredientCreateRequest(UUID.randomUUID().toString(), "Coffee Beans", 100)
        );

        DrinkCreateRequest drinkCreateRequest = new DrinkCreateRequest(UUID.randomUUID().toString(), "Espresso", "Brew coffee and enjoy!", ingredients);

        try {
            drinkService.addDrink(drinkCreateRequest).exceptionally(ex -> {
                System.err.println("Failed to add drink: " + ex.getMessage());
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ingredientService.addIngredient(new IngredientCreateRequest(UUID.randomUUID().toString(), "Milk", 200)).exceptionally(ex -> {
                System.err.println("Failed to add ingredient: " + ex.getMessage());
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
