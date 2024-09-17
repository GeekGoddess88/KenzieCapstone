package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.IngredientInterface;
import com.kenzie.capstone.service.model.IngredientRecord;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class IngredientCachingDAO implements IngredientDAO {

    private final IngredientDAO ingredientDAO;
    private final CacheClient cacheClient;
    private final ObjectMapper objectMapper;

    @Inject
    public IngredientCachingDAO(IngredientDAO ingredientDAO, CacheClient cacheClient) {
        this.ingredientDAO = ingredientDAO;
        this.cacheClient = cacheClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public IngredientRecord findById(String id) {
        String cacheKey = "Ingredient_" + id;
        Optional<String> cachedValue = cacheClient.getValue(cacheKey);

        if (cachedValue.isPresent()) {
            try {
                return objectMapper.readValue(cachedValue.get(), IngredientRecord.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        IngredientRecord ingredientRecord = ingredientDAO.findById(id);
        if (ingredientRecord != null) {
            try {
                cacheClient.setValue(cacheKey, 3600, objectMapper.writeValueAsString(ingredientRecord));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ingredientRecord;
    }

    @Override
    public List<IngredientRecord> findAll() {
        return ingredientDAO.findAll();
    }

    @Override
    public void save(IngredientRecord ingredientRecord) {
        ingredientDAO.save(ingredientRecord);
        try {
            cacheClient.setValue("ingredient_" + ingredientRecord.getId(), 3600, objectMapper.writeValueAsString(ingredientRecord));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        ingredientDAO.delete(id);
        cacheClient.invalidate("Ingredient_" + id);
    }
}
