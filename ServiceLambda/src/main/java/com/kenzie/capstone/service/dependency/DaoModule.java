package com.kenzie.capstone.service.dependency;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.*;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.util.DynamoDbClientProvider;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides DynamoDBMapper instance to DAO classes.
 */
@Module(includes = CachingModule.class)
public class DaoModule {

    @Provides
    @Singleton
    @Named("DynamoDBMapper")
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

    @Singleton
    @Provides
    @Named("DrinkNonCachingDAO")
    public DrinkNonCachingDAO provideDrinkNonCachingDAO(@Named("DynamoDBMapper") DynamoDBMapper dynamoDBMapper) {
        return new DrinkNonCachingDAO(dynamoDBMapper);
    }

    @Singleton
    @Provides
    @Named("IngredientNonCachingDAO")
    public IngredientDAO provideIngredientNonCachingDAO(@Named("DynamoDBMapper") DynamoDBMapper dynamoDBMapper) {
        return new IngredientNonCachingDAO(dynamoDBMapper);
    }

    @Singleton
    @Provides
    @Named("DrinkDao")
    public DrinkDAO provideDrinkDAO(@Named("CacheClient") CacheClient cacheClient, ObjectMapper objectMapper, @Named("DrinkNonCachingDAO") DrinkNonCachingDAO nonCachingDAO) {
        return new DrinkCachingDAO(cacheClient, objectMapper, nonCachingDAO);
    }


    @Singleton
    @Provides
    @Named("IngredientDao")
    public IngredientDAO provideIngredientDAO(@Named("IngredientNonCachingDAO") IngredientNonCachingDAO nonCachingDAO, ObjectMapper objectMapper, @Named("CacheClient") CacheClient cacheClient) {
        return new IngredientCachingDAO(nonCachingDAO, objectMapper, cacheClient);
    }


}
