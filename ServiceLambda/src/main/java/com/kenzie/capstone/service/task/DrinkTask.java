package com.kenzie.capstone.service.task;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.model.DrinkInterface;
import com.kenzie.capstone.service.model.DrinkRecord;
import dagger.Component;
import org.apache.logging.log4j.core.config.Scheduled;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrinkTask {

    private final DrinkDAO drinkDAO;
    private final LambdaServiceClient lambdaServiceClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public DrinkTask(DrinkDAO drinkDAO, LambdaServiceClient lambdaServiceClient) {
        this.drinkDAO = drinkDAO;
        this.lambdaServiceClient = new LambdaServiceClient();
    }

    public void saveDrinkAsync(DrinkRecord drinkRecord) {
        executorService.submit(() -> {
            try {
                drinkDAO.save(drinkRecord);
                lambdaServiceClient.saveDrink(drinkRecord);
                System.out.println("Drink saved asynchronously");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteDrinkAsync(String drinkId) {
        executorService.submit(() -> {
            try {
                drinkDAO.delete(drinkId);
                lambdaServiceClient.deleteDrinkById(drinkId);
                System.out.println("Drink deleted asynchronously");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
