package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.IngredientRecord;

import javax.inject.Inject;
import java.util.List;

public class IngredientNonCachingDAO implements IngredientDAO {

    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public IngredientNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public IngredientRecord findById(String id) {
        return dynamoDBMapper.load(IngredientRecord.class, id);
    }

    @Override
    public List<IngredientRecord> findAll() {
        return dynamoDBMapper.scan(IngredientRecord.class, new DynamoDBScanExpression());
    }

    @Override
    public void save(IngredientRecord ingredientRecord) {
        dynamoDBMapper.save(ingredientRecord);
    }

    @Override
    public void delete(String id) {
        IngredientRecord ingredientRecord = findById(id);
        if (ingredientRecord != null) {
            dynamoDBMapper.delete(ingredientRecord);
        }
    }
}
