package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;

import java.util.UUID;

public class IngredientConverter {

    public IngredientResponse toIngredientResponse(IngredientRecord ingredientRecord) {
        if (ingredientRecord == null) return null;
        return new IngredientResponse(ingredientRecord.getId(), ingredientRecord.getName(),
                ingredientRecord.getQuantity()
        );
    }

    public IngredientRecord toIngredientRecord(IngredientCreateRequest ingredientCreateRequest) {
        if (ingredientCreateRequest == null) return null;
        return new IngredientRecord(UUID.randomUUID().toString(), ingredientCreateRequest.getName(),
                ingredientCreateRequest.getQuantity());
    }

    public IngredientRecord toIngredientRecord(IngredientUpdateRequest ingredientUpdateRequest) {
        if (ingredientUpdateRequest == null) return null;
        return new IngredientRecord(ingredientUpdateRequest.getId(), ingredientUpdateRequest.getName(),
                ingredientUpdateRequest.getQuantity());
    }
}
