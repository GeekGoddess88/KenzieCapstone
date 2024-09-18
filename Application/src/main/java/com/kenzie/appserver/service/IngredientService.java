package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.IngredientDAO;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.IngredientRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class IngredientService {

    private final IngredientDAO cachingDAO;
    private final IngredientDAO nonCachingDAO;
    private final ExecutorService executorService;

    public IngredientService(IngredientDAO cachingDAO, IngredientDAO nonCachingDAO, ExecutorService executorService) {
        this.cachingDAO = cachingDAO;
        this.nonCachingDAO = nonCachingDAO;
        this.executorService = executorService;
    }

    public IngredientRecord findById(String id) {
        return cachingDAO.findById(id);
    }

    public List<IngredientRecord> findAll() {
        return nonCachingDAO.findAll();
    }

    public void save(IngredientRecord ingredient) {
        executorService.submit(() -> {
            cachingDAO.save(ingredient);
        });
    }

    public void update(String id, IngredientRecord ingredient) {
        executorService.submit(() -> {
            nonCachingDAO.update(id, ingredient);
            cachingDAO.update(id, ingredient);
        });
    }

    public void delete(String id) {
        executorService.submit(() -> {
            nonCachingDAO.delete(id);
            cachingDAO.delete(id);
        });
    }
}
