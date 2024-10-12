package com.kenzie.appserver.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.*;
import com.kenzie.capstone.service.model.converter.DrinkConverter;
import com.kenzie.capstone.service.model.converter.IngredientConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class LambdaServiceConfig {

    @Bean
    public LambdaService lambdaService(DrinkDAO drinkDAO, IngredientDAO ingredientDAO, IngredientConverter ingredientConverter, DrinkConverter drinkConverter) {
        return new LambdaService(drinkDAO, ingredientDAO, drinkConverter, ingredientConverter);
    }

    @Bean
    public DrinkDAO drinkDAO(DrinkNonCachingDAO drinkNonCachingDAO, CacheClient cacheClient, ObjectMapper objectMapper) {
        return new DrinkCachingDAO(cacheClient, objectMapper, drinkNonCachingDAO);
    }

    @Bean
    public DrinkNonCachingDAO drinkNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        return new DrinkNonCachingDAO(dynamoDBMapper);
    }

    @Bean
    public IngredientDAO ingredientDAO(IngredientNonCachingDAO ingredientNonCachingDAO, CacheClient cacheClient, ObjectMapper objectMapper) {
        return new IngredientCachingDAO(ingredientNonCachingDAO, objectMapper, cacheClient);
    }

    @Bean
    public IngredientNonCachingDAO ingredientNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        return new IngredientNonCachingDAO(dynamoDBMapper);
    }

    @Bean
    public CacheClient cacheClient(JedisPool jedisPool) {
        return new CacheClient(jedisPool);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public DrinkConverter drinkConverter(IngredientConverter ingredientConverter) {
        return new DrinkConverter(ingredientConverter);
    }

    @Bean
    public IngredientConverter ingredientConverter() {
        return new IngredientConverter();
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "localhost", 6379);
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

}
