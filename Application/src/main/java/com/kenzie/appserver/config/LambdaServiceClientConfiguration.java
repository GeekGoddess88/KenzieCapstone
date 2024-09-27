package com.kenzie.appserver.config;


import com.kenzie.capstone.service.client.EndpointUtility;
import com.kenzie.capstone.service.client.LambdaServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaServiceClientConfiguration {

    private EndpointUtility endpointUtility;

    @Bean
    public LambdaServiceClient lambdaServiceClient() {
        return new LambdaServiceClient(endpointUtility);
    }
}
