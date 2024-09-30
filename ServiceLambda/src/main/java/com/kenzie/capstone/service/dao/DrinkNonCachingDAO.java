package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.DrinkRecord;

import javax.inject.Inject;
import java.util.*;

public class DrinkNonCachingDAO implements DrinkDAO {

    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public DrinkNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public Optional<DrinkRecord> findById(String id) {
        DrinkRecord drinkRecord = dynamoDBMapper.load(DrinkRecord.class, id);
        return Optional.ofNullable(drinkRecord);
    }

    @Override
    public List<DrinkRecord> findAll() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(DrinkRecord.class, scanExpression);
    }

    @Override
    public void update(String id, DrinkRecord drinkRecord) {
        dynamoDBMapper.save(drinkRecord);
    }

    @Override
    public void save(DrinkRecord drinkRecord) {
        dynamoDBMapper.save(drinkRecord);
    }

    @Override
    public void delete(String id) {
        DrinkRecord drinkRecord = new DrinkRecord();
        drinkRecord.setId(id);
        dynamoDBMapper.delete(drinkRecord);
    }
}
