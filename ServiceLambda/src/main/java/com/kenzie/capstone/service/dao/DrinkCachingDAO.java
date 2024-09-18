package com.kenzie.capstone.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final DrinkDAO nonCachingDAO;
    private final CacheClient cacheClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public DrinkCachingDAO(DrinkDAO nonCachingDAO, CacheClient cacheClient) {
        this.nonCachingDAO = nonCachingDAO;
        this.cacheClient = cacheClient;
    }

    @Override
    public DrinkRecord findById(String id) {
        String cacheKey = "drink:" + id;
        Optional<String> cachedValue = cacheClient.getValue(cacheKey);

        if (cachedValue.isPresent()) {
            try {
                return objectMapper.readValue(cachedValue.get(), DrinkRecord.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrinkRecord drinkRecord = nonCachingDAO.findById(id);
        if (drinkRecord != null) {
            try {
                cacheClient.setValue(cacheKey, 3600, objectMapper.writeValueAsString(drinkRecord));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return drinkRecord;
    }

    @Override
    public List<DrinkRecord> findAll() {
        return nonCachingDAO.findAll();
    }

    @Override
    public void save(DrinkRecord drinkRecord) {
        nonCachingDAO.save(drinkRecord);
        try {
            cacheClient.setValue("drink:" + drinkRecord.getId(), 3600, objectMapper.writeValueAsString(drinkRecord));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String id, DrinkRecord drinkRecord) {
        nonCachingDAO.update(id, drinkRecord);
        try {
            cacheClient.setValue("drink:" + drinkRecord.getId(), 3600, objectMapper.writeValueAsString(drinkRecord));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        nonCachingDAO.delete(id);
        cacheClient.invalidate("drink:" + id);
    }
}
