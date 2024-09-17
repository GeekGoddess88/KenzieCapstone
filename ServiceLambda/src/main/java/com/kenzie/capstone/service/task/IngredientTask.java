package com.kenzie.capstone.service.task;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.dao.IngredientCachingDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.IngredientInterface;
import com.kenzie.capstone.service.model.IngredientRecord;
import org.apache.logging.log4j.core.config.Scheduled;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IngredientTask {

    private final IngredientDAO ingredientDAO;
    private final LambdaServiceClient lambdaServiceClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public IngredientTask(IngredientDAO ingredientDAO, LambdaServiceClient lambdaServiceClient) {
        this.ingredientDAO = ingredientDAO;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public void saveIngredientAsync(IngredientRecord ingredientRecord) {
        executorService.submit(() -> {
            try {
                ingredientDAO.save(ingredientRecord);
                lambdaServiceClient.saveIngredient(ingredientRecord);
                System.out.println("Ingredient saved asynchronously");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteIngredientAsync(String ingredientId) {
        executorService.submit(() -> {
            try {
                ingredientDAO.delete(ingredientId);
                lambdaServiceClient.deleteIngredientById(ingredientId);
                System.out.println("Ingredient deleted asynchronously");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
