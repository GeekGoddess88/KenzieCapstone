package com.kenzie.capstone.service.model.converter;

import com.kenzie.capstone.service.model.*;

import java.util.UUID;

public class IngredientConverter {

    public IngredientResponse toIngredientResponse(IngredientRecord ingredientRecord) {
        if (ingredientRecord == null) return null;
        return new IngredientResponse(ingredientRecord.getId(), ingredientRecord.getName(),
                ingredientRecord.getQuantity()
        );
    }

    public IngredientRecord toIngredientRecordFromCreateRequest(IngredientCreateRequest ingredientCreateRequest) {
        if (ingredientCreateRequest == null) return null;
        return new IngredientRecord(UUID.randomUUID().toString(), ingredientCreateRequest.getName(),
                ingredientCreateRequest.getQuantity());
    }

    public IngredientRecord toIngredientRecordFromResponse(IngredientResponse ingredientResponse) {
        if (ingredientResponse == null) return null;
        return new IngredientRecord(ingredientResponse.getId(), ingredientResponse.getName(), ingredientResponse.getQuantity());
    }

    public IngredientRecord toIngredientRecordFromUpdateRequest(IngredientUpdateRequest ingredientUpdateRequest) {
        if (ingredientUpdateRequest == null) return null;
        return new IngredientRecord(ingredientUpdateRequest.getId(), ingredientUpdateRequest.getName(),
                ingredientUpdateRequest.getQuantity());
    }
}
