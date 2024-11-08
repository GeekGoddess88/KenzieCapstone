package com.kenzie.appserver;



import com.kenzie.appserver.service.DrinkService;
import com.kenzie.appserver.service.IngredientService;

import com.kenzie.capstone.service.model.DrinkCreateRequest;
import com.kenzie.capstone.service.model.IngredientCreateRequest;


import com.kenzie.capstone.service.model.IngredientRecord;
import com.kenzie.capstone.service.model.IngredientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) throws RuntimeException {
        try {
            loadInitialData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load initial data on application start.", e);
        }
    }

    private void loadInitialData() throws IOException {
        try {
            if (drinkService.getAllDrinks().isCompletedExceptionally()) {
                List<CompletableFuture<IngredientResponse>> ingredientFutures = List.of(
                        ingredientService.addIngredient(new IngredientCreateRequest("1", "Coffee Beans", 500)),
                        ingredientService.addIngredient(new IngredientCreateRequest("2", "Water", 500)),
                        ingredientService.addIngredient(new IngredientCreateRequest("3", "Milk", 100)),
                        ingredientService.addIngredient(new IngredientCreateRequest("4", "Cream", 200)),
                        ingredientService.addIngredient(new IngredientCreateRequest("5", "Sugar", 300)),
                        ingredientService.addIngredient(new IngredientCreateRequest("6", "Caramel", 100)),
                        ingredientService.addIngredient(new IngredientCreateRequest("7", "Chocolate", 100)),
                        ingredientService.addIngredient(new IngredientCreateRequest("8", "Ice", 500)),
                        ingredientService.addIngredient(new IngredientCreateRequest("9", "Vanilla", 200)),
                        ingredientService.addIngredient(new IngredientCreateRequest("10", "Sweetener", 300))
                                                                                       );

                CompletableFuture.allOf(ingredientFutures.toArray(new CompletableFuture[0])).join();

                drinkService.addDrink(new DrinkCreateRequest(
                        "1",
                        "Caffe Latte",
                        "Grind coffee beans real fine and brew. Stir in optional sweetener, steam and froth milk until light layer of foam and gently pour steamed milk into brewed coffee.",
                        List.of(new IngredientCreateRequest("1", "Coffee Beans", 50),
                                new IngredientCreateRequest("2", "Water", 100),
                                new IngredientCreateRequest("3", "Milk", 150))
                )).get();

            drinkService.addDrink(new DrinkCreateRequest(
                    "2",
                    "Iced Caramel Macchiato",
                    "Brew espresso. Add caramel syrup, ice, and milk. Top with espresso and drizzle caramel on top.",
                    List.of(new IngredientCreateRequest("1", "Coffee Beans", 50),
                            new IngredientCreateRequest("6", "Caramel", 20),
                            new IngredientCreateRequest("8", "Ice", 100),
                            new IngredientCreateRequest("3", "Milk", 200))
            )).get();
        }
    } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException("Error loading initial data", e);
        }
    }
}
