package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.DrinkRecord;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class DrinkCachingDAO implements DrinkDAO {

    private final CacheClient cacheClient;
    private final ObjectMapper objectMapper;
    private final DrinkNonCachingDAO drinkNonCachingDAO;
    private static final int CACHE_TTL = 3600;
    private static final String DRINK_CACHE_KEY_PREFIX = "drink_";

    public DrinkCachingDAO(CacheClient cacheClient, ObjectMapper objectMapper, DrinkNonCachingDAO drinkNonCachingDAO) {
        this.cacheClient = cacheClient;
        this.objectMapper = objectMapper;
        this.drinkNonCachingDAO = drinkNonCachingDAO;
    }

    @Override
    public Optional<DrinkRecord> findById(String id) {
        String cachedDrink = cacheClient.getValue(DRINK_CACHE_KEY_PREFIX + id);
        if (cachedDrink != null) {
            try {
                DrinkRecord drinkRecord = objectMapper.readValue(cachedDrink, DrinkRecord.class);
                return Optional.of(drinkRecord);
            } catch (IOException e) {
                throw new RuntimeException("Failed to serialize drink record", e);
            }
        }

        Optional<DrinkRecord> drinkRecordOptional = drinkNonCachingDAO.findById(id);
        drinkRecordOptional.ifPresent(drinkRecord -> cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + id, CACHE_TTL, toJson(drinkRecord)));
        return drinkRecordOptional;
    }


    @Override
    public List<DrinkRecord> findAll() {
        List<DrinkRecord> drinks = drinkNonCachingDAO.findAll();
        drinks.forEach(drink -> {
            cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + drink.getId(), CACHE_TTL, toJson(drink));
        });
        return drinks;
    }

    @Override
    public void save(DrinkRecord drink) {
        drinkNonCachingDAO.save(drink);
        cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + drink.getId(), CACHE_TTL, toJson(drink));
    }

    @Override
    public void update(String id, DrinkRecord drink) {
        drinkNonCachingDAO.update(id, drink);
        cacheClient.setValue(DRINK_CACHE_KEY_PREFIX + id, CACHE_TTL, toJson(drink));
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
