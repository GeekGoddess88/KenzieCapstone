package com.kenzie.capstone.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.kenzie.capstone.service.dao.*;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientRecord;
import com.kenzie.capstone.service.task.IngredientTask;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class LambdaService {

    private final DrinkDAO drinkDAO;
    private final IngredientDAO ingredientDAO;
    private final ExecutorService executorService;

    @Inject
    public LambdaService(@Named("DrinkDAO") DrinkDAO drinkDAO, @Named("IngredientDAO") IngredientDAO ingredientDAO, ExecutorService executorService) {
        this.drinkDAO = drinkDAO;
        this.ingredientDAO = ingredientDAO;
        this.executorService = executorService;
    }

    public Future<List<DrinkRecord>> findAllDrinks() {
        return executorService.submit(() -> {
            return drinkDAO.findAll();
        });
    }

    public void findDrinkById(String id) {
        executorService.submit(() -> {
            return drinkDAO.findById(id);
        });
    }

    public DrinkRecord save(DrinkRecord drink) {
        drinkDAO.save(drink);
        return drink;
    }

    public void updateDrink(String id, DrinkRecord drink) {
        executorService.submit(() -> {
            drinkDAO.update(id, drink);
        });
    }

    public void deleteDrink(String id) {
        drinkDAO.delete(id);
    }

    public IngredientRecord save(IngredientRecord ingredient) {
        IngredientTask task = new IngredientTask(ingredientDAO, ingredient);
        executorService.submit(task);
    }

    public void updateIngredient(String id, IngredientRecord ingredient) {
        executorService.submit(() -> {
            ingredientDAO.update(id, ingredient);
        });
    }

    public void deleteIngredient(String id) {
        ingredientDAO.delete(id);
    }

    public Future<List<IngredientRecord>> findAll() {
        return executorService.submit(() -> {
            return ingredientDAO.findAll();
        });
    }
}
