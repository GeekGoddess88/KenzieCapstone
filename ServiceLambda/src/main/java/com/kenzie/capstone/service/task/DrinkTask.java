package com.kenzie.capstone.service.task;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.model.DrinkRecord;
import dagger.Component;
import org.apache.logging.log4j.core.config.Scheduled;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


public class DrinkTask {
/*
    private final DrinkDAO drinkCachingDAO;
    private final DrinkDAO drinkNonCachingDAO;

    @Inject
    public DrinkTask(DrinkDAO drinkCachingDAO, DrinkDAO drinkNonCachingDAO ) {
        this.drinkCachingDAO = drinkCachingDAO;
        this.drinkNonCachingDAO = drinkNonCachingDAO;
    }

    public CompletableFuture<Void> refreshDrinkCacheAsync() {
        List<DrinkRecord> drinksFromDB = drinkNonCachingDAO.findAll();
        drinksFromDB.forEach(drinkCachingDAO::save);
        System.out.println("Asynchronous drink cache refreshed");
        return CompletableFuture.completedFuture(null);
*/
}
