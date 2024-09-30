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
import com.kenzie.capstone.service.model.IngredientCreateRequest;
import com.kenzie.capstone.service.model.IngredientResponse;


public class AddIngredient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Gson gson = new GsonBuilder().create();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            IngredientCreateRequest createRequest = gson.fromJson(input.getBody(), IngredientCreateRequest.class);
            ServiceComponent serviceComponent = DaggerServiceComponent.create();
            LambdaService lambdaService = serviceComponent.provideLambdaService();
            IngredientResponse ingredientResponse = lambdaService.addIngredient(createRequest);

            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(ingredientResponse));
        } catch (Exception e) {
            return response
                    .withStatusCode(500)
                    .withBody(gson.toJson(e));
        }
    }
}
