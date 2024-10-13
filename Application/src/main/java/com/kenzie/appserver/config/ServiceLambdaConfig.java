package com.kenzie.appserver.config;

import com.kenzie.capstone.service.LambdaService;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.dao.DrinkCachingDAO;
import com.kenzie.capstone.service.dao.IngredientCachingDAO;
import com.kenzie.capstone.service.dao.DrinkNonCachingDAO;
import com.kenzie.capstone.service.dao.IngredientNonCachingDAO;
import com.kenzie.capstone.service.model.converter.DrinkConverter;
import com.kenzie.capstone.service.model.converter.IngredientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ServiceLambdaConfig {

    @Bean
    @Qualifier("drinkDAO")
    public DrinkDAO drinkDAO(DrinkNonCachingDAO drinkNonCachingDAO, CacheClient cacheClient, ObjectMapper objectMapper) {
        return new DrinkCachingDAO(cacheClient, objectMapper, drinkNonCachingDAO);
    }

    @Bean
    public DrinkNonCachingDAO drinkNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        return new DrinkNonCachingDAO(dynamoDBMapper);
    }

    @Bean
    @Qualifier("ingredientDAO")
    public IngredientDAO ingredientDAO(IngredientNonCachingDAO ingredientNonCachingDAO, CacheClient cacheClient,ObjectMapper objectMapper) {
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
    @Qualifier("drinkConverter")
    public DrinkConverter drinkConverter(IngredientConverter ingredientConverter) {
        return new DrinkConverter(ingredientConverter);
    }

    @Bean
    @Qualifier("ingredientConverter")
    public IngredientConverter ingredientConverter() {
        return new IngredientConverter();
    }

    @Bean
    @Qualifier("jedisPool")
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "localhost", 6379);
    }

    @Bean
    @Qualifier("dynamoDBMapper")
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Autowired
    public LambdaService lambdaService(DrinkDAO drinkDAO, IngredientDAO ingredientDAO, DrinkConverter drinkConverter, IngredientConverter ingredientConverter) {
        return new LambdaService(drinkDAO, ingredientDAO, drinkConverter, ingredientConverter);
    }

}
