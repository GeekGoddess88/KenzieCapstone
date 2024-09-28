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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("DrinkService") DrinkService drinkService, @Named("IngredientService") IngredientService ingredientService) {
        return new LambdaService(drinkService, ingredientService);
    }

    @Provides
    @Singleton
    public DrinkService provideDrinkService(DrinkCachingDAO drinkCachingDAO, LambdaServiceClient lambdaServiceClient, DrinkConverter drinkConverter) {
        return new DrinkService(drinkCachingDAO, lambdaServiceClient, drinkConverter);
    }

    @Provides
    @Singleton
    public IngredientService provideIngredientService(IngredientCachingDAO ingredientCachingDAO, LambdaServiceClient lambdaServiceClient, IngredientConverter ingredientConverter) {
        return new IngredientService(ingredientCachingDAO, lambdaServiceClient, ingredientConverter);
    }
}


