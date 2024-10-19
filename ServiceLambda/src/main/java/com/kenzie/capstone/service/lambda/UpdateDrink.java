package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.model.DrinkResponse;
import com.kenzie.capstone.service.model.DrinkUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LambdaService lambdaService;
    private final Gson gson = new Gson();

    @Autowired
    public UpdateDrink(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            String drinkId = input.getPathParameters().get("id");
            DrinkUpdateRequest drinkUpdateRequest = gson.fromJson(input.getBody(), DrinkUpdateRequest.class);

            DrinkResponse drinkResponse = lambdaService.updateDrink(drinkId, drinkUpdateRequest);
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(drinkResponse));

        } catch (Exception e) {
            return response
                    .withStatusCode(500)
                    .withBody(gson.toJson("Error updating drink: " + e.getMessage()));
        }
    }
}