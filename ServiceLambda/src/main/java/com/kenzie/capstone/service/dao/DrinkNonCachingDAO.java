package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.List;
import java.util.Optional;

public class DrinkNonCachingDAO implements DrinkDAO {

    private final DynamoDBMapper dynamoDBMapper;

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
        DrinkRecord drinkRecord = dynamoDBMapper.load(DrinkRecord.class, id);
        if (drinkRecord != null) {
            dynamoDBMapper.delete(drinkRecord);
        }
    }
}
