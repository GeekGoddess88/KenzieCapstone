package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.model.DrinkInterface;
import com.kenzie.capstone.service.model.DrinkRecord;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DrinkCachingDAO implements DrinkDAO{

    private final DrinkNonCachingDAO nonCachingDAO;
    private final CacheClient cacheClient;

    @Inject
    public DrinkCachingDAO(DrinkNonCachingDAO nonCachingDAO, CacheClient cacheClient) {
        this.nonCachingDAO = nonCachingDAO;
        this.cacheClient = cacheClient;
    }

    @Override
    public DrinkRecord findById(String id) {
        Optional<String> cachedValue = cacheClient.getValue(id);

        if (cachedValue.isPresent()) {
            return deserializeDrink(cachedValue.get());
        }

        DrinkRecord drinkRecord = nonCachingDAO.findById(id);
        cacheClient.setValue(id, 3600, serializeDrink(drinkRecord));
        return drinkRecord;
    }

    @Override
    public List<DrinkRecord> findAll() {
        return nonCachingDAO.findAll();
    }

    @Override
    public void save(DrinkRecord drinkRecord) {
        nonCachingDAO.save(drinkRecord);
        cacheClient.setValue(drinkRecord.getId(), 3600, serializeDrink(drinkRecord));
    }

    @Override
    public void update(String id, DrinkRecord drinkRecord) {
        nonCachingDAO.update(id, drinkRecord);
        cacheClient.setValue(id, 3600, serializeDrink(drinkRecord));
    }

    @Override
    public void delete(String id) {
        nonCachingDAO.delete(id);
        cacheClient.invalidate(id);
    }

    private String serializeDrink(DrinkRecord drinkRecord) {
        return new Gson().toJson(drinkRecord);
    }

    private DrinkRecord deserializeDrink(String json) {
        return new Gson().fromJson(json, DrinkRecord.class);
    }
}
