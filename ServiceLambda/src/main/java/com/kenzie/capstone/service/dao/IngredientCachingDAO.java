package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.converter.IngredientConverter;
import com.kenzie.capstone.service.model.Ingredient;
import com.kenzie.capstone.service.model.IngredientRecord;
import dagger.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class IngredientCachingDAO implements IngredientDAO {

    private final IngredientNonCachingDAO ingredientNonCachingDAO;
    private final ObjectMapper objectMapper;
    private final CacheClient cacheClient;

    private static final String INGREDIENT_CACHE_KEY_PREFIX = "ingredient_";

    @Inject
    public IngredientCachingDAO(IngredientNonCachingDAO ingredientNonCachingDAO, ObjectMapper objectMapper, CacheClient cacheClient) {
        this.ingredientNonCachingDAO = ingredientNonCachingDAO;
        this.objectMapper = objectMapper;
        this.cacheClient = cacheClient;
    }

    @Override
    public Optional<IngredientRecord> findById(String id) {
        String cacheKey = INGREDIENT_CACHE_KEY_PREFIX + id;
        String ingredientJson = cacheClient.getValue(INGREDIENT_CACHE_KEY_PREFIX + id).orElse(null);
        if (ingredientJson != null) {
            try {
                return Optional.of(objectMapper.readValue(ingredientJson, IngredientRecord.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Optional<IngredientRecord> ingredientRecord = ingredientNonCachingDAO.findById(id);
        ingredientRecord.ifPresent(record -> cacheClient.setValue(cacheKey, 3600, toJson(record)));
        return ingredientRecord;

    }

    @Override
    public List<IngredientRecord> findAll() {
        List<IngredientRecord> ingredients = ingredientNonCachingDAO.findAll();
        ingredients.forEach(ingredient -> cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + ingredient.getId(), 3600, toJson(ingredient)));
        return ingredients;
    }

    @Override
    public void save(IngredientRecord ingredientRecord) {
        ingredientNonCachingDAO.save(ingredientRecord);
        cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + id, 3600, toJson(ingredientRecord));
    }

    @Override
    public void update(String id, IngredientRecord ingredientRecord) {
        ingredientNonCachingDAO.update(id, ingredientRecord);
        cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + id, 3600, toJson(ingredientRecord));
    }

    @Override
    public void delete(String id) {
        ingredientNonCachingDAO.delete(id);
        cacheClient.invalidate(INGREDIENT_CACHE_KEY_PREFIX + id);
    }

    private String toJson(IngredientRecord ingredientRecord) {
        try {
            return objectMapper.writeValueAsString(ingredientRecord);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing IngredientRecord", e);
        }
    }
}
