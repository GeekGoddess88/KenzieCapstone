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
        if (drinkCreateRequest == null) return null;

        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(UUID.randomUUID().toString());
        drinkRecord.setName(drinkCreateRequest.getName());
        drinkRecord.setIngredients(drinkCreateRequest.getIngredients());
        drinkRecord.setRecipe(drinkCreateRequest.getRecipe());
        return drinkRecord;
    }

    public DrinkRecord toDrinkRecord(DrinkUpdateRequest drinkUpdateRequest) {
        if (drinkUpdateRequest == null) return null;

        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(drinkUpdateRequest.getId());
        drinkRecord.setName(drinkUpdateRequest.getName());
        drinkRecord.setIngredients(drinkUpdateRequest.getIngredients());
        drinkRecord.setRecipe(drinkUpdateRequest.getRecipe());
        return drinkRecord;
    }

    public DrinkRecord toDrinkRecord(DrinkResponse drinkResponse) {
        if (drinkResponse == null) return null;
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(drinkResponse.getId());
        drinkRecord.setName(drinkResponse.getName());
        drinkRecord.setIngredients(drinkResponse.getIngredients());
        drinkRecord.setRecipe(drinkResponse.getRecipe());
        return drinkRecord;
    }
}
