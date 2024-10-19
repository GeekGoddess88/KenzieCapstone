package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.IngredientRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientCachingDAO implements IngredientDAO {

    private final IngredientNonCachingDAO ingredientNonCachingDAO;
    private final ObjectMapper objectMapper;
    private final CacheClient cacheClient;
    private static final int CACHE_TTL = 3600;
    private static final String INGREDIENT_CACHE_KEY_PREFIX = "ingredient_";

    public IngredientCachingDAO(IngredientNonCachingDAO ingredientNonCachingDAO, ObjectMapper objectMapper, CacheClient cacheClient) {
        this.ingredientNonCachingDAO = ingredientNonCachingDAO;
        this.objectMapper = objectMapper;
        this.cacheClient = cacheClient;
    }

    @Override
    public Optional<IngredientRecord> findById(String id) {
        String cachedIngredient = cacheClient.getValue(INGREDIENT_CACHE_KEY_PREFIX + id);
        if (cachedIngredient != null) {
            try {
                IngredientRecord ingredientRecord = objectMapper.readValue(cachedIngredient, IngredientRecord.class);
                return Optional.of(ingredientRecord);
            } catch (IOException e) {
                throw new RuntimeException("Error deserializing ingredient", e);
            }
        }

        Optional<IngredientRecord> ingredientRecord = ingredientNonCachingDAO.findById(id);
        ingredientRecord.ifPresent(record -> cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + id, CACHE_TTL, toJson(record)));
        return ingredientRecord;
    }

    @Override
    public List<IngredientRecord> findAll() {
        List<IngredientRecord> ingredients = ingredientNonCachingDAO.findAll();
        ingredients.forEach(ingredient -> cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + ingredient.getId(), CACHE_TTL, toJson(ingredient)));
        return ingredients;
    }

    @Override
    public void save(IngredientRecord ingredientRecord) {
        ingredientNonCachingDAO.save(ingredientRecord);
        cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + ingredientRecord.getId(), CACHE_TTL, toJson(ingredientRecord));
    }

    @Override
    public void update(String id, IngredientRecord ingredientRecord) {
        ingredientNonCachingDAO.update(id, ingredientRecord);
        cacheClient.setValue(INGREDIENT_CACHE_KEY_PREFIX + id, CACHE_TTL, toJson(ingredientRecord));
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
