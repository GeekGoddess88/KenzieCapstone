package com.kenzie.marketing.referral.service.dependency;

import com.kenzie.marketing.referral.service.LambdaService;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Declares the dependency roots that Dagger will provide.
 */
@Singleton
@Component(modules = {DaoModule.class, ServiceModule.class})
public interface ServiceComponent {
    LambdaService provideLambdaService();
}
