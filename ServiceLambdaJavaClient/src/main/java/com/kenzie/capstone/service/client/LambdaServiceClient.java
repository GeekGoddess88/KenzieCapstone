package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.List;


public class LambdaServiceClient {

    private final LambdaService lambdaService;
    private final EndpointUtility endpointUtility;


    public LambdaServiceClient(LambdaService lambdaService, EndpointUtility endpointUtility) {
        this.lambdaService = lambdaService;
        this.endpointUtility = endpointUtility;
    }

    public DrinkRecord findDrinkById(String drinkId) throws Exception {
        String functionName = endpointUtility.getDrinkFunctionName();
        return lambdaService.findById(functionName, drinkId);
    }

    public List<DrinkRecord> findAllDrinks() throws Exception {
        String functionName = endpointUtility.getFindAllDrinksFunctionName();
        return lambdaService.findAllDrinks(functionName);
    }

    public void saveDrink(DrinkRecord drinkRecord) throws Exception {
        String functionName = endpointUtility.getSaveDrinkFunctionName();
        lambdaService.saveDrink(functionName, drinkRecord);
    }

    public void updateDrink(DrinkRecord drinkRecord) throws Exception {
        String functionName = endpointUtility.getUpdateDrinkFunctionName();
        lambdaService.updateDrink(functionName, drinkRecord);
    }

    public void deleteDrinkById(String drinkId) throws Exception {
        String functionName = endpointUtility.getDeleteDrinkFunctionName();
        lambdaService.deleteDrinkById(functionName, drinkId);
    }

    public IngredientRecord findById(String ingredientId) throws Exception {
        String functionName = endpointUtility.getIngredientFunctionName();
        return lambdaService.findIngredientById(functionName, ingredientId);
    }

    public List<IngredientRecord> findAll() throws Exception {
        String functionName = endpointUtility.getFindAllIngredientsFunctionName();
        return lambdaService.findAllIngredients(functionName);
    }

    public void saveIngredient(IngredientRecord ingredientRecord) throws Exception {
        String functionName = endpointUtility.getSaveIngredientFunctionName();
        lambdaService.saveIngredient(functionName, ingredientRecord);
    }

    public void updateIngredient(IngredientRecord ingredientRecord) throws Exception {
        String functionName = endpointUtility.getUPdateIngredientFunctionName();
        lambdaService.updateIngredient(functionName, ingredientRecord);
    }

    public void deleteIngredientById(String ingredientId) throws Exception {
        String functionName = endpointUtility.getDeleteIngredientFunctionName();
        lambdaService.deleteIngredientById(functionName, ingredientId);
    }


}
