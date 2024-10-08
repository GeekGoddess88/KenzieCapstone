package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.converter.IngredientConverter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DrinkConverter {

    public DrinkResponse toDrinkResponse(DrinkRecord drinkRecord) {
        if (drinkRecord == null) return null;
        return new DrinkResponse(
                drinkRecord.getId(),
                drinkRecord.getName(),
                drinkRecord.getIngredients(),
                drinkRecord.getRecipe()
        );
    }

    public DrinkRecord toDrinkRecord(DrinkCreateRequest drinkCreateRequest) {
        List<IngredientRecord> ingredientRecords = drinkCreateRequest.getIngredients().stream()
                .map(this::toIngredientRecord)
                .collect(Collectors.toList());

        return new DrinkRecord(
                UUID.randomUUID().toString(),
                drinkCreateRequest.getName(),
                ingredientRecords,
                drinkCreateRequest.getRecipe()
        );
    }

    public DrinkRecord toDrinkRecord(DrinkUpdateRequest drinkUpdateRequest) {
        if (drinkUpdateRequest == null) return null;
        return new DrinkRecord(drinkUpdateRequest.getId(), drinkUpdateRequest.getName(),
                drinkUpdateRequest.getIngredients(), drinkUpdateRequest.getRecipe());
    }

    private IngredientRecord toIngredientRecord(IngredientCreateRequest ingredientCreateRequest) {
        return new IngredientRecord(
                ingredientCreateRequest.getId(),
                ingredientCreateRequest.getName(),
                ingredientCreateRequest.getQuantity()
        );
    }
}
