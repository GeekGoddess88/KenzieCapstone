package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.converter.DrinkConverter;
import com.kenzie.capstone.service.converter.IngredientConverter;
import com.kenzie.capstone.service.dao.*;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;


@Module(
    includes = {DaoModule.class,
        CachingModule.class}
)
public class ServiceModule {


    @Provides
    @Singleton
    LambdaService provideLambdaService(@Named("DrinkDAO") DrinkDAO drinkDAO,
                                              @Named("IngredientDAO") IngredientDAO ingredientDAO,
                                              @Named("DrinkConverter") DrinkConverter drinkConverter,
                                              @Named("IngredientConverter") IngredientConverter ingredientConverter) {
        return new LambdaService(drinkDAO, ingredientDAO,  drinkConverter, ingredientConverter);
    }

    @Provides
    @Singleton
    IngredientConverter provideIngredientConverter() {
        return new IngredientConverter();
    }

    @Provides
    @Singleton
    DrinkConverter provideDrinkConverter() {
        return new DrinkConverter();
    }


}


