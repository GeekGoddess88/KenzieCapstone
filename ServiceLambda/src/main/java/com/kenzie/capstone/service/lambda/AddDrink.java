package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.LambdaService;

import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.DrinkCreateRequest;
import com.kenzie.capstone.service.model.DrinkResponse;


public class AddDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Gson gson = new GsonBuilder().create();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            DrinkCreateRequest drinkCreateRequest = gson.fromJson(request.getBody(), DrinkCreateRequest.class);

            ServiceComponent serviceComponent = DaggerServiceComponent.create();
            LambdaService lambdaService = serviceComponent.provideLambdaService();

            DrinkResponse drinkResponse = lambdaService.addDrink(drinkCreateRequest);

            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(drinkResponse));
        } catch (Exception e) {
            return response
                    .withStatusCode(500)
                    .withBody(gson.toJson(e));
        }
    }
}
