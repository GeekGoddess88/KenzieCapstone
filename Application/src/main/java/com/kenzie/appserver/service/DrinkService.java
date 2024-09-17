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
    private final LambdaServiceClient lambdaServiceClient;


    public DrinkService(DrinkDAO cachingDAO, DrinkDAO nonCachingDAO, LambdaServiceClient lambdaServiceClient) {
        this.cachingDAO = cachingDAO;
        this.nonCachingDAO = nonCachingDAO;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public DrinkRecord findById(String id) {
        DrinkRecord drinkRecord = cachingDAO.findById(id);
        if (drinkRecord == null) {
            drinkRecord = nonCachingDAO.findById(id);
            if (drinkRecord != null) {
                cachingDAO.save(drinkRecord);
            }
        }
        return drinkRecord;
    }

    public List<DrinkRecord> findAllDrinks() {
        return nonCachingDAO.findAll();
    }

    public void saveDrink(DrinkRecord drink) {
        cachingDAO.save(drink);
        nonCachingDAO.save(drink);
        try {
            lambdaServiceClient.saveDrink(drink);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDrink(String id, DrinkRecord drink) {
        cachingDAO.update(id, drink);
        nonCachingDAO.update(id, drink);
        try {
            lambdaServiceClient.saveDrink(drink);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDrink(String id) {
        cachingDAO.delete(id);
        nonCachingDAO.delete(id);
        try {
            lambdaServiceClient.deleteDrinkById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
