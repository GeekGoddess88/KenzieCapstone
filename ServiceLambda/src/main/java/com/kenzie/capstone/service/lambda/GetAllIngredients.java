package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.IngredientResponse;


import java.util.List;

public class GetAllIngredients implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        Gson gson = new GsonBuilder().create();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        try {
            ServiceComponent serviceComponent = DaggerServiceComponent.create();
            LambdaService lambdaService = serviceComponent.provideLambdaService();
            List<IngredientResponse> ingredientResponses = lambdaService.getAllIngredients();
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(ingredientResponses));
        } catch (Exception e) {
            return response
                    .withStatusCode(500)
                    .withBody(gson.toJson(e));
        }
    }
}
