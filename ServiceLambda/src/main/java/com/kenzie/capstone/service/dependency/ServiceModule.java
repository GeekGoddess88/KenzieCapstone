package com.kenzie.capstone.service.dependency;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.converter.DrinkConverter;
import com.kenzie.capstone.service.converter.IngredientConverter;
import com.kenzie.capstone.service.dao.*;


import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {
    @Singleton
    @Provides
    public LambdaService provideLambdaService(DrinkDAO drinkDAO, IngredientDAO ingredientDAO, ExecutorService executorService, DrinkConverter drinkConverter, IngredientConverter ingredientConverter) {
        return new LambdaService(drinkDAO, ingredientDAO, executorService, drinkConverter, ingredientConverter);
    }

    public ExecutorService provideExecutorService() {
        return Executors.newCachedThreadPool();
    }
}


