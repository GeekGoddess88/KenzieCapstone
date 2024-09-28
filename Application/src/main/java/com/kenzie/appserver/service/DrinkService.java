package com.kenzie.appserver.service;




import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.*;


import com.kenzie.capstone.service.dao.DrinkCachingDAO;
import com.kenzie.capstone.service.converter.DrinkConverter;

import com.kenzie.capstone.service.model.DrinkResponse;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.DrinkUpdateRequest;
import com.kenzie.capstone.service.model.DrinkCreateRequest;
import com.kenzie.capstone.service.client.EndpointUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrinkService {

    private final DrinkCachingDAO drinkCachingDAO;
    private final DrinkConverter drinkConverter;
    private final LambdaServiceClient lambdaServiceClient;

    @Inject
    public DrinkService(DrinkCachingDAO drinkCachingDAO, LambdaServiceClient lambdaServiceClient, DrinkConverter drinkConverter) {
        this.drinkCachingDAO = drinkCachingDAO;
        this.lambdaServiceClient = lambdaServiceClient;
        this.drinkConverter = drinkConverter;
    }

    public DrinkResponse addDrink(DrinkCreateRequest drinkCreateRequest) throws IOException {
        DrinkResponse response = lambdaServiceClient.addDrink(drinkCreateRequest);
        DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(response);
        drinkCachingDAO.save(drinkRecord);
        return response;
    }

    public DrinkResponse getDrinkById(String drinkId) throws IOException {
        Optional<DrinkRecord> drinkRecord = drinkCachingDAO.findById(drinkId);
        if (drinkRecord.isPresent()) {
            return drinkConverter.toDrinkResponse(drinkRecord.get());
        }
        DrinkResponse drinkResponse = lambdaServiceClient.getDrinkById(drinkId);
        DrinkRecord drink = drinkConverter.toDrinkRecord(drinkResponse);
        drinkCachingDAO.save(drink);
        return drinkResponse;
    }

    public List<DrinkResponse> getAllDrinks() throws IOException {
        DrinkResponse[] drinkResponses = lambdaServiceClient.getAllDrinks();

        List<DrinkRecord> drinkRecords = Arrays.stream(drinkResponses)
                .map(drinkConverter::toDrinkRecord)
                .collect(Collectors.toList());
        drinkRecords.forEach(drinkCachingDAO::save);
        return Arrays.asList(drinkResponses);
    }

    public DrinkResponse updateDrink(String drinkId, DrinkUpdateRequest drinkUpdateRequest) throws IOException {
        DrinkResponse drinkResponse = lambdaServiceClient.updateDrink(drinkId, drinkUpdateRequest);
        DrinkRecord drinkRecord = drinkConverter.toDrinkRecord(drinkResponse);
        drinkCachingDAO.update(drinkId, drinkRecord);
        return drinkResponse;
    }

    public DeleteDrinkResponse deleteDrinkById(String drinkId) throws IOException {
        DeleteDrinkResponse deleteResponse = lambdaServiceClient.deleteDrinkById(drinkId);
        drinkCachingDAO.delete(drinkId);
        return deleteResponse;
    }
}
