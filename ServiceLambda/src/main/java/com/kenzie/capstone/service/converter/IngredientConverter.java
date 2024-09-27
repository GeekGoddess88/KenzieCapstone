package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;

public class IngredientConverter {

    public IngredientResponse toIngredientResponse(IngredientRecord ingredient) {
        if (ingredient == null) return null;

        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getQuantity()
        );
    }

    public IngredientRecord toIngredientRecord(IngredientCreateRequest ingredientCreateRequest) {
        if (ingredientCreateRequest == null) return null;
        IngredientRecord ingredientRecord = new IngredientRecord();
        ingredientRecord.setName(ingredientCreateRequest.getName());
        ingredientRecord.setQuantity(ingredientCreateRequest.getQuantity());
        return ingredientRecord;
    }

    public IngredientRecord toIngredientRecord(IngredientUpdateRequest ingredientUpdateRequest) {
        if (ingredientUpdateRequest == null) return null;

        IngredientRecord ingredientRecord = new IngredientRecord();
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
