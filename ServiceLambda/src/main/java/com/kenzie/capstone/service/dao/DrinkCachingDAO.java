package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenzie.capstone.service.converter.DrinkConverter;
import com.kenzie.capstone.service.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.caching.CacheClient;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class DrinkCachingDAO implements DrinkDAO {

    private final CacheClient cacheClient;
    private final ObjectMapper objectMapper;
    private final DrinkNonCachingDAO drinkNonCachingDAO;

    private static final String DRINK_CACHE_KEY_PREFIX = "drink_";

    @Inject
    public DrinkCachingDAO(CacheClient cacheClient, ObjectMapper objectMapper, DrinkNonCachingDAO drinkNonCachingDAO) {
        this.cacheClient = cacheClient;
        this.objectMapper = objectMapper;
        this.drinkNonCachingDAO = drinkNonCachingDAO;
    }

    @Override
    public Optional<DrinkRecord> findById(String id) {

            String drinkJson = cacheClient.getValue(DRINK_CACHE_KEY_PREFIX + id).orElse(null);
            if (drinkJson != null) {
                try {
                    DrinkRecord cachedDrink = objectMapper.readValue(drinkJson, DrinkRecord.class);
                    return Optional.of(cachedDrink);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
            Optional<DrinkRecord> drinkRecord = drinkNonCachingDAO.findById(id);
            drinkRecord.ifPresent(record -> {
                cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + id, 3600, toJson(record));
            });
            return drinkRecord;
    }

    @Override
    public List<DrinkRecord> findAll() {
        List<DrinkRecord> drinks = drinkNonCachingDAO.findAll();
        drinks.forEach(drink -> {
            cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + drink.getId(), 3600, toJson(drink));
        });
        return drinks;
    }

    @Override
    public void save(DrinkRecord drink) {
        drinkNonCachingDAO.save(drink);
        cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + drink.getId(), 3600, toJson(drink));
    }

    @Override
    public void update(String id, DrinkRecord drink) {
        drinkNonCachingDAO.update(id, drink);
        cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + id, 3600, toJson(drink));
    }

    @Override
    public void delete(String id) {
        drinkNonCachingDAO.delete(id);
        cacheClient.invalidate(DRINK_CACHE_KEY_PREFIX + id);
    }

    private String toJson(DrinkRecord drinkRecord) {
        try {
            return objectMapper.writeValueAsString(drinkRecord);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing DrinkRecord", e);
        }
    }
}
