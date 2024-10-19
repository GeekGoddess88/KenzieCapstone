package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.model.IngredientCreateRequest;
import com.kenzie.capstone.service.model.IngredientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddIngredient implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

    private final LambdaService lambdaService;
    private final Gson gson = new Gson();

    @Autowired
    public AddIngredient(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            IngredientCreateRequest createRequest = gson.fromJson(input.getBody(), IngredientCreateRequest.class);
            IngredientResponse ingredientResponse = lambdaService.addIngredient(createRequest);
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(ingredientResponse));
        } catch (Exception e) {
            return response.withStatusCode(500).withBody(gson.toJson("Error adding ingredient: " + e.getMessage()));
        }
    }
}
