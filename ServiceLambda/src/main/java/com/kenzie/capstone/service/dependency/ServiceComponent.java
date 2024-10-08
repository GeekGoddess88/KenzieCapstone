package com.kenzie.capstone.service.dependency;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.converter.DrinkConverter;
import com.kenzie.capstone.service.converter.IngredientConverter;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.IngredientDAO;
import dagger.Component;

import redis.clients.jedis.Jedis;



/**
 * Declares the dependency roots that Dagger will provide.
 */
@Component(modules = {DaoModule.class, ServiceModule.class, CachingModule.class})
public interface ServiceComponent {
    LambdaService provideLambdaService();
    DrinkDAO provideDrinkDAO();
    IngredientDAO provideIngredientDAO();
    IngredientConverter provideIngredientConverter();
    DrinkConverter provideDrinkConverter();
    Jedis provideJedis();

}
