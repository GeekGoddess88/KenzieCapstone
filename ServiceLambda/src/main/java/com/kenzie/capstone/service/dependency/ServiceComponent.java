package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;

import com.kenzie.capstone.service.dao.DrinkCachingDAO;
import com.kenzie.capstone.service.dao.DrinkDAO;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Declares the dependency roots that Dagger will provide.
 */
@Singleton
@Component(modules = {DaoModule.class, ServiceModule.class, CachingModule.class})
public interface ServiceComponent {
    LambdaService provideLambdaService();

}
