package com.kenzie.capstone.service.dependency;


import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.*;
import com.kenzie.capstone.service.util.DynamoDbClientProvider;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides DynamoDBMapper instance to DAO classes.
 */
@Module
public class DaoModule {

    @Singleton
    @Provides
    public DrinkDAO provideDrinkNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        return new DrinkNonCachingDAO(dynamoDBMapper);
    }

    @Singleton
    @Provides
    public DrinkDAO provideDrinkCachingDAO(DrinkDAO nonCachingDAO, CacheClient cacheClient) {
        return new DrinkCachingDAO(nonCachingDAO, cacheClient);
    }

    @Singleton
    @Provides
    public IngredientDAO provideIngredientNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        return new IngredientNonCachingDAO(dynamoDBMapper);
    }

    @Singleton
    @Provides
    public IngredientDAO provideIngredientCachingDAO(IngredientDAO nonCachingDAO, CacheClient cacheClient) {
        return new IngredientCachingDAO(nonCachingDAO, cacheClient);
    }

    @Provides
    @Singleton
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }

}
