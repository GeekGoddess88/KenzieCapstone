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
import com.kenzie.capstone.service.model.*;


public class UpdateDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Gson gson = new GsonBuilder().create();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            String drinkId = input.getPathParameters().get("id");
            DrinkUpdateRequest drinkUpdateRequest = gson.fromJson(input.getBody(), DrinkUpdateRequest.class);

            ServiceComponent serviceComponent = DaggerServiceComponent.create();
            LambdaService lambdaService = serviceComponent.provideLambdaService();

            DrinkResponse drinkResponse = lambdaService.updateDrink(drinkId, drinkUpdateRequest);


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
