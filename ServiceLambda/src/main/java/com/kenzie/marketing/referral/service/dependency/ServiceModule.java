package com.kenzie.marketing.referral.service.dependency;

import com.kenzie.marketing.referral.service.LambdaService;
import com.kenzie.marketing.referral.service.dao.ExampleDao;

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
    public LambdaService provideLambdaService(@Named("ExampleDao") ExampleDao exampleDao) {
        return new LambdaService(exampleDao);
    }
}

