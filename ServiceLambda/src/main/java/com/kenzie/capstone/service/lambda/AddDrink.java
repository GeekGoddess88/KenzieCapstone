package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.Gson;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.model.DrinkCreateRequest;
import com.kenzie.capstone.service.model.DrinkResponse;
import dagger.Component;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;


public class AddDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LambdaService lambdaService;

    @Inject
    public AddDrink(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Gson gson = new Gson();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            DrinkCreateRequest drinkCreateRequest = gson.fromJson(input.getBody(), DrinkCreateRequest.class);
            DrinkResponse drinkResponse = lambdaService.addDrink(drinkCreateRequest);
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(drinkResponse));
        } catch (Exception e) {
            return response
                    .withStatusCode(500)
                    .withBody(gson.toJson("Error: " + e.getMessage()));
        }
    }
}
