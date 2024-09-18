package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.DrinkDAO;

import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.DrinkRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class DrinkService {

    private final DrinkDAO cachingDAO;
    private final DrinkDAO nonCachingDAO;
    private final ExecutorService executorService;


    public DrinkService(DrinkDAO cachingDAO, DrinkDAO nonCachingDAO, ExecutorService executorService) {
        this.cachingDAO = cachingDAO;
        this.nonCachingDAO = nonCachingDAO;
        this.executorService = executorService;
    }

    public DrinkRecord findById(String id) {
        return cachingDAO.findById(id);
    }

    public List<DrinkRecord> findAllDrinks() {
        return nonCachingDAO.findAll();
    }

    public void saveDrink(DrinkRecord drink) {
        executorService.submit(() -> {
            cachingDAO.save(drink);
        });
    }

    public void updateDrink(String id, DrinkRecord drink) {
        executorService.submit(() -> {
            nonCachingDAO.update(id, drink);
            cachingDAO.update(id, drink);
        });
    }

    public void deleteDrink(String id) {
        executorService.submit(() -> {
            nonCachingDAO.delete(id);
            cachingDAO.delete(id);
        });
    }
}
