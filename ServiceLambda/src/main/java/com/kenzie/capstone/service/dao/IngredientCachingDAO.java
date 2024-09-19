package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.IngredientInterface;
import com.kenzie.capstone.service.model.IngredientRecord;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class IngredientCachingDAO implements IngredientDAO {

    private final IngredientNonCachingDAO ingredientDAO;
    private final CacheClient cacheClient;

    @Inject
    public IngredientCachingDAO(IngredientNonCachingDAO ingredientDAO, CacheClient cacheClient) {
        this.ingredientDAO = ingredientDAO;
        this.cacheClient = cacheClient;
    }

    @Override
    public IngredientRecord findById(String id) {
        Optional<String> cachedValue = cacheClient.getValue(id);

        if (cachedValue.isPresent()) {
            return deserializeIngredient(cachedValue.get());
        }

        IngredientRecord ingredientRecord = ingredientDAO.findById(id);
        cacheClient.setValue(id, 3600, serializeIngredient(ingredientRecord));
        return ingredientRecord;
    }

    @Override
    public List<IngredientRecord> findAll() {
        return ingredientDAO.findAll();
    }

    @Override
    public void save(IngredientRecord ingredientRecord) {
        ingredientDAO.save(ingredientRecord);
        cacheClient.setValue(ingredientRecord.getId(), 3600, serializeIngredient(ingredientRecord));
    }

    @Override
    public void update(String id, IngredientRecord ingredientRecord) {
        ingredientDAO.update(id, ingredientRecord);
        cacheClient.setValue(id, 3600, serializeIngredient(ingredientRecord));
    }

    @Override
    public void delete(String id) {
        ingredientDAO.delete(id);
        cacheClient.invalidate(id);
    }

    private String serializeIngredient(IngredientRecord ingredientRecord) {
        return new Gson().toJson(ingredientRecord);
    }

    private IngredientRecord deserializeIngredient(String json) {
        return new Gson().fromJson(json, IngredientRecord.class);
    }
}
