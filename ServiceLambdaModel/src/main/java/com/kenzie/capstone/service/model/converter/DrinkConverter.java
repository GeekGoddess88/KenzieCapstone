package com.kenzie.capstone.service.model.converter;

import com.kenzie.capstone.service.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DrinkConverter {

    private final IngredientConverter ingredientConverter;

    public DrinkConverter(IngredientConverter ingredientConverter) {
        this.ingredientConverter = ingredientConverter;
    }

    public DrinkResponse toDrinkResponse(DrinkRecord drinkRecord) {
        if (drinkRecord == null) return null;
        List<IngredientResponse> ingredientResponses = drinkRecord.getIngredients().stream()
                .map(ingredientConverter::toIngredientResponse)
                .collect(Collectors.toList());

        return new DrinkResponse(
                drinkRecord.getId(),
                drinkRecord.getName(),
                ingredientResponses,
                drinkRecord.getRecipe()
        );
    }

    public DrinkRecord toDrinkRecord(DrinkCreateRequest drinkCreateRequest) {
        List<IngredientRecord> ingredientRecords = drinkCreateRequest.getIngredients().stream()
                .map(ingredientConverter::toIngredientRecordFromCreateRequest)
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

        List<IngredientRecord> ingredientRecords = drinkUpdateRequest.getIngredients().stream()
                .map(ingredientConverter::toIngredientRecordFromResponse)
                .collect(Collectors.toList());

        return new DrinkRecord(
                drinkUpdateRequest.getId(),
                drinkUpdateRequest.getName(),
                ingredientRecords,
                drinkUpdateRequest.getRecipe()
        );
    }
}
