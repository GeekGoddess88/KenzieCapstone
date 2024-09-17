package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.IngredientDAO;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.List;


public class IngredientService {

    private final IngredientDAO cachingDAO;
    private final IngredientDAO nonCachingDAO;
    private final LambdaServiceClient lambdaClient;

    public IngredientService(IngredientDAO cachingDAO, IngredientDAO nonCachingDAO, LambdaServiceClient lambdaClient) {
        this.cachingDAO = cachingDAO;
        this.nonCachingDAO = nonCachingDAO;
        this.lambdaClient = lambdaClient;
    }

    public IngredientRecord findById(String id) {
        IngredientRecord ingredientRecord = cachingDAO.findById(id);
        if (ingredientRecord == null) {
            ingredientRecord = nonCachingDAO.findById(id);
            if (ingredientRecord != null) {
                cachingDAO.save(ingredientRecord);
            }
        }
        return ingredientRecord;
    }

    public List<IngredientRecord> findAll() {
        return nonCachingDAO.findAll();
    }

    public void saveIngredient(IngredientRecord ingredient) {
        cachingDAO.save(ingredient);
        nonCachingDAO.save(ingredient);
        try {
            lambdaClient.saveIngredient(ingredient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateIngredient(String id, IngredientRecord ingredient) {
        cachingDAO.update(id, ingredient);
        nonCachingDAO.update(id, ingredient);
        try {
            lambdaClient.saveIngredient(ingredient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteIngredient(String id) {
        cachingDAO.delete(id);
        nonCachingDAO.delete(id);
        try {
            lambdaClient.deleteIngredientById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
