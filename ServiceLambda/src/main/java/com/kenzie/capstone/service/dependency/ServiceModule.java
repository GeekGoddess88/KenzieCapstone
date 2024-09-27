package com.kenzie.capstone.service.dependency;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.*;


import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("DrinkDAO") DrinkDAO drinkDAO, @Named("IngredientDAO") IngredientDAO ingredientDAO, @Named("ExecutorService") ExecutorService executorService) {
        return new LambdaService(drinkDAO, ingredientDAO, executorService);
    }
}


