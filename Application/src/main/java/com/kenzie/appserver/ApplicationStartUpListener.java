package com.kenzie.appserver;


import com.kenzie.capstone.service.task.DrinkTask;
import com.kenzie.capstone.service.task.IngredientTask;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


@Component
public class ApplicationStartUpListener {

    private final DrinkTask drinkTask;
    private final IngredientTask ingredientTask;

    @Inject
    public ApplicationStartUpListener(DrinkTask drinkTask, IngredientTask ingredientTask) {
        this.drinkTask = drinkTask;
        this.ingredientTask = ingredientTask;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Perform any application start-up tasks
        warmUpCaches();
    }

    public void warmUpCaches() {
        drinkTask.refreshDrinkCache();
        ingredientTask.refreshIngredientCache();
    }
}
