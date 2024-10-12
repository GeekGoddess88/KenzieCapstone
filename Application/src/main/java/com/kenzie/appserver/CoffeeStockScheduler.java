package com.kenzie.appserver;

import com.kenzie.appserver.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoffeeStockScheduler {

    private final IngredientService ingredientService;

    @Autowired
    public CoffeeStockScheduler(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkAndReplenishStock() {
    ingredientService.checkAndReplenishStock();
    }
}
