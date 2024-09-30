package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.DrinkConverter;
import com.kenzie.capstone.service.converter.IngredientConverter;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class LambdaService {

    private final DrinkDAO drinkDAO;
    private final IngredientDAO ingredientDAO;
    private final ExecutorService executorService;
    private final DrinkConverter drinkConverter;
    private final IngredientConverter ingredientConverter;

    @Inject
    public LambdaService(DrinkDAO drinkDAO, IngredientDAO ingredientDAO, ExecutorService executorService, DrinkConverter drinkConverter, IngredientConverter ingredientConverter) {
        this.drinkDAO = drinkDAO;
        this.ingredientDAO = ingredientDAO;
        this.executorService = executorService;
        this.drinkConverter = drinkConverter;
        this.ingredientConverter = ingredientConverter;
    }

    public DrinkResponse getDrinkById(String drinkId) {
        Optional<DrinkRecord> drinkRecord = drinkDAO.findById(drinkId);
        return drinkRecord.map(drinkConverter::toDrinkResponse).orElseThrow(() -> new RuntimeException("Drink not found"));
    }

    public List<DrinkResponse> getAllDrinks() {
        List<DrinkRecord> drinkRecords = drinkDAO.findAll();
        List<Future<DrinkResponse>> futures = drinkRecords.stream()
                .map(record -> executorService.submit(() -> drinkConverter.toDrinkResponse(record)))
                .collect(Collectors.toList());

        return futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error retrieving drink responses", e);
            }
        }).collect(Collectors.toList());
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) {
        DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkCreateRequest);
        drinkDAO.save(drinkRecord);
        return drinkConverter.toDrinkResponse(drinkRecord);
    }

    public DrinkResponse updateDrink(String id, DrinkUpdateRequest drinkUpdateRequest) {
        DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkUpdateRequest);
        drinkDAO.update(id, drinkRecord);
        return drinkConverter.toDrinkResponse(drinkRecord);
    }

    public DeleteDrinkResponse deleteDrinkById(String id) {
        drinkDAO.delete(id);
        return new DeleteDrinkResponse(id, "Drink deleted successfully");
    }

    public IngredientResponse getIngredientById(String ingredientId) {
        Optional<IngredientRecord> ingredientRecord = ingredientDAO.findById(ingredientId);
        return ingredientRecord.map(ingredientConverter::toIngredientResponse).orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public List<IngredientResponse> getAllIngredients() {
        List<IngredientRecord> ingredientRecords = ingredientDAO.findAll();
        List<Future<IngredientResponse>> futures = ingredientRecords.stream()
                .map(record -> executorService.submit(() -> ingredientConverter.toIngredientResponse(record)))
                .collect(Collectors.toList());

        return futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error retrieving ingredient responses", e);
            }
        }).collect(Collectors.toList());
    }

    public IngredientResponse addIngredient(IngredientCreateRequest ingredientCreateRequest) {
        IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientCreateRequest);
        ingredientDAO.save(ingredientRecord);
        return ingredientConverter.toIngredientResponse(ingredientRecord);
    }

    public IngredientResponse updateIngredient(String id, IngredientUpdateRequest ingredientUpdateRequest) {
        IngredientRecord ingredientRecord = ingredientConverter.toIngredientRecord(ingredientUpdateRequest);
        ingredientDAO.update(id, ingredientRecord);
        return ingredientConverter.toIngredientResponse(ingredientRecord);
    }

    public DeleteIngredientResponse deleteIngredientById(String id) {
            ingredientDAO.delete(id);
            return new DeleteIngredientResponse(id, "Ingredient deleted successfully");
    }
}
