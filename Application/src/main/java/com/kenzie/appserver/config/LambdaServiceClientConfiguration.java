package com.kenzie.appserver.config;


import com.kenzie.capstone.service.client.EndpointUtility;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    @Qualifier("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("LambdaServiceClient-Executor-");
        executor.initialize();
        return executor;
    }

    @Autowired
    public LambdaServiceClient lambdaServiceClient(EndpointUtility endpointUtility, @Qualifier("taskExecutor") Executor taskExecutor) {
        return new LambdaServiceClient(endpointUtility, taskExecutor);
    }
}
