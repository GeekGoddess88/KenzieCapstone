package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.DrinkConverter;
import com.kenzie.capstone.service.converter.IngredientConverter;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LambdaService {

    private final DrinkDAO drinkDAO;
    public IngredientDAO ingredientDAO;
    private final DrinkConverter drinkConverter;
    public IngredientConverter ingredientConverter;

    @Inject
    public LambdaService(DrinkDAO drinkDAO, IngredientDAO ingredientDAO, DrinkConverter drinkConverter, IngredientConverter ingredientConverter) {
        this.drinkDAO = drinkDAO;
        this.ingredientDAO = ingredientDAO;
        this.drinkConverter = drinkConverter;
        this.ingredientConverter = ingredientConverter;
    }

    public DrinkResponse getDrinkById(String drinkId) {
        Optional<DrinkRecord> drinkRecord = drinkDAO.findById(drinkId);
        return drinkRecord
                .map(drinkConverter::toDrinkResponse)
                .orElseThrow(() -> new RuntimeException("Could not find drink with id " + drinkId));
    }

    public List<DrinkResponse> getAllDrinks() {
        List<DrinkRecord> drinkRecords = drinkDAO.findAll();
        return drinkRecords.stream()
                .map(drinkConverter::toDrinkResponse)
                .collect(Collectors.toList());
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) {
        DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkCreateRequest);
        drinkDAO.save(drinkRecord);
        return drinkConverter.toDrinkResponse(drinkRecord);
    }

    public DrinkResponse updateDrink(String drinkId, DrinkUpdateRequest drinkUpdateRequest) {
        DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkUpdateRequest);
        drinkDAO.update(drinkId, drinkRecord);
        return drinkConverter.toDrinkResponse(drinkRecord);
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) {
        drinkDAO.delete(drinkId);
        return new DeleteDrinkResponse(drinkId, "Deleted");
    }

    public IngredientResponse getIngredientById(String ingredientId) {
        Optional<IngredientRecord> ingredientRecord = ingredientDAO.findById(ingredientId);
        return ingredientRecord
                .map(ingredientConverter::toIngredientResponse)
                .orElseThrow(() -> new RuntimeException("Could not find ingredient with id " + ingredientId));
    }

    public List<IngredientResponse> getAllIngredients() {
        List<IngredientRecord> ingredientRecords = ingredientDAO.findAll();
        return ingredientRecords.stream()
                .map(ingredientConverter::toIngredientResponse)
                .collect(Collectors.toList());
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) {
        IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientCreateRequest);
        ingredientDAO.save(ingredientRecord);
        return ingredientConverter.toIngredientResponse(ingredientRecord);
    }

    public IngredientResponse updateIngredient(String ingredientId, IngredientUpdateRequest ingredientUpdateRequest) {
        IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientUpdateRequest);
        ingredientDAO.update(ingredientId, ingredientRecord);
        return ingredientConverter.toIngredientResponse(ingredientRecord);
    }

    public DeleteIngredientResponse deleteIngredientById(String ingredientId) {
        ingredientDAO.delete(ingredientId);
        return new DeleteIngredientResponse(ingredientId, "Deleted");
    }

}





