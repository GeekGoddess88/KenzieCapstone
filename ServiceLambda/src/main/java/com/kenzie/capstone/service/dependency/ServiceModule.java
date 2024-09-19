package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.dao.ExampleDao;

import com.kenzie.capstone.service.dao.IngredientDAO;
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
    public LambdaService provideLambdaService(@Named("DrinkDAO") DrinkDAO drinkDAO, IngredientDAO ingredientDAO) {
        return new LambdaService(drinkDAO, ingredientDAO);
    }
}

