package com.kenzie.appserver.service;


import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.inject.Inject;
import java.util.List;

@Service
public class DrinkService {

    private final LambdaServiceClient lambdaServiceClient;


    @Inject
    public DrinkService(LambdaServiceClient lambdaServiceClient) {
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) {
        try {
            return lambdaServiceClient.addDrink(drinkCreateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DrinkResponse getDrinkById(String drinkId) {
        try {
            return lambdaServiceClient.getDrinkById(drinkId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DrinkResponse> getAllDrinks() {
        try {
            List<DrinkResponse> drinks = lambdaServiceClient.getAllDrinks();
            return drinks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DrinkResponse updateDrink(String drinkId, DrinkUpdateRequest drinkUpdateRequest) {
        try {
            return lambdaServiceClient.updateDrink(drinkId, drinkUpdateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) {
        try {
            return lambdaServiceClient.deleteDrinkById(drinkId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
