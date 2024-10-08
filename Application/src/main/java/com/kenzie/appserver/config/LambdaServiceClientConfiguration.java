package com.kenzie.appserver.config;


import com.kenzie.capstone.service.client.EndpointUtility;
import com.kenzie.capstone.service.client.LambdaServiceClient;


import dagger.internal.InjectedFieldSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.inject.Named;


@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    public LambdaServiceClient lambdaServiceClient(EndpointUtility endpointUtility, @Named("LambdaServiceClient-Executor")TaskExecutor taskExecutor) {
        return new LambdaServiceClient(endpointUtility, taskExecutor);

    }

    @Bean
    public EndpointUtility endpointUtility() {
        return new EndpointUtility();
    }

    @Bean
    @Named("LambdaServiceClient-Executor")
    public TaskExecutor lambdaServiceClientTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("LambdaServiceClient-Executor");
        executor.initialize();
        return executor;
    }
}
