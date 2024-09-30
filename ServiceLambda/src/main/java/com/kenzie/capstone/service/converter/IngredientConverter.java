package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;

import java.util.UUID;

public class IngredientConverter {

    public IngredientResponse toIngredientResponse(IngredientRecord ingredientRecord) {
        if (ingredientRecord == null) return null;
        return new IngredientResponse(
                ingredientRecord.getId(),
                ingredientRecord.getName(),
                ingredientRecord.getQuantity()
        );
    }

    public IngredientRecord toIngredientRecord(IngredientCreateRequest ingredientCreateRequest) {
        if (ingredientCreateRequest == null) return null;
        IngredientRecord ingredientRecord = new IngredientRecord();
        ingredientRecord.setId(UUID.randomUUID().toString());
        ingredientRecord.setName(ingredientCreateRequest.getName());
        ingredientRecord.setQuantity(ingredientCreateRequest.getQuantity());
        return ingredientRecord;
    }

    public IngredientRecord toIngredientRecord(IngredientUpdateRequest ingredientUpdateRequest) {
        if (ingredientUpdateRequest == null) return null;
        IngredientRecord ingredientRecord = new IngredientRecord();
        ingredientRecord.setId(ingredientUpdateRequest.getId());
        ingredientRecord.setName(ingredientUpdateRequest.getName());
        ingredientRecord.setQuantity(ingredientUpdateRequest.getQuantity());
        return ingredientRecord;
    }

    public IngredientRecord toIngredientRecord(IngredientResponse ingredientResponse) {
        if (ingredientResponse == null) return null;
        IngredientRecord ingredientRecord = new IngredientRecord();
        ingredientRecord.setId(ingredientResponse.getId());
        ingredientRecord.setName(ingredientResponse.getName());
        ingredientRecord.setQuantity(ingredientResponse.getQuantity());
        return ingredientRecord;
    }
}
