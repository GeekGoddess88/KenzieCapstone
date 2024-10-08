package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.Gson;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.model.DrinkResponse;
import com.kenzie.capstone.service.model.IngredientResponse;
import dagger.Component;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class GetAllDrinks implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LambdaService lambdaService;

    @Inject
    public GetAllDrinks(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Gson gson = new Gson();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            List<DrinkResponse> drinkResponses = lambdaService.getAllDrinks();
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(drinkResponses));
        } catch (Exception e) {
            return response
                    .withStatusCode(500)
                    .withBody(gson.toJson("Error: " + e.getMessage()));
        }
    }
}
