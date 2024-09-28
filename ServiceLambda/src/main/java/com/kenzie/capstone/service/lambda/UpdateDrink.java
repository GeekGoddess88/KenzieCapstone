package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.IngredientResponse;
import com.kenzie.capstone.service.model.IngredientUpdateRequest;

public class UpdateDrink implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LambdaService lambdaService;

    public UpdateDrink() {
        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        this.lambdaService = serviceComponent.provideLambdaService();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String id = input.getPathParameters().get("id");
            IngredientUpdateRequest ingredientUpdateRequest = new ObjectMapper().readValue(input.getBody(), IngredientUpdateRequest.class);
            IngredientResponse updatedIngredient = lambdaService.updateIngredient(id, ingredientUpdateRequest);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(new ObjectMapper().writeValueAsString(updatedIngredient));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(e.getMessage());
        }
    }
}
