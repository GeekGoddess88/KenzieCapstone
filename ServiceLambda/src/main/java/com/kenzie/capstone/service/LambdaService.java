package com.kenzie.capstone.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientRecord;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LambdaService {

    private final DrinkDAO drinkDAO;
    private final IngredientDAO ingredientDAO;

    @Inject
    public LambdaService(DrinkDAO drinkDAO, IngredientDAO ingredientDAO) {
        this.drinkDAO = drinkDAO;
        this.ingredientDAO = ingredientDAO;
    }

    public DrinkRecord getDrinkById(String id) {
        return drinkDAO.findById(id);
    }

    public IngredientRecord getIngredientById(String id) {
        return ingredientDAO.findById(id);
    }

    public List<DrinkRecord> getAllDrinks() {
        return drinkDAO.findAll();
    }

    public List<IngredientRecord> getAllIngredients() {
        return ingredientDAO.findAll();
    }

    public void addDrink(DrinkRecord drink) {
        drinkDAO.save(drink);
    }

    public void addIngredient(IngredientRecord ingredient) {
        ingredientDAO.save(ingredient);
    }

    public void updateDrink(String id, DrinkRecord drink) {
        drinkDAO.update(id, drink);
    }

    public void updateIngredient(String id, IngredientRecord ingredient) {
        ingredientDAO.update(id, ingredient);
    }

    public void deleteDrink(String id) {
        drinkDAO.delete(id);
    }

    public void deleteIngredient(String id) {
        ingredientDAO.delete(id);
    }
}
