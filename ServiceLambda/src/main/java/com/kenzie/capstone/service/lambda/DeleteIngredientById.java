package com.kenzie.capstone.service.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.model.DeleteIngredientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteIngredientById implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LambdaService lambdaService;
    private final Gson gson = new Gson();

    @Autowired
    public DeleteIngredientById(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            String id = request.getPathParameters().get("id");
            DeleteIngredientResponse deleteIngredientResponse = lambdaService.deleteIngredientById(id);
            return response.withStatusCode(200).withBody(gson.toJson(deleteIngredientResponse));
        } catch (Exception e) {
           return response.withStatusCode(500).withBody(gson.toJson("Error deleting ingredient: " + e.getMessage()));
        }
    }
}
