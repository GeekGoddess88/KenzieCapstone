package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.kenzie.capstone.service.model.IngredientRecord;
import dagger.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Component
public class IngredientNonCachingDAO implements IngredientDAO {

    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public IngredientNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public Optional<IngredientRecord> findById(String id) {
        IngredientRecord ingredientRecord = dynamoDBMapper.load(IngredientRecord.class, id);
        return Optional.ofNullable(ingredientRecord);
    }

    @Override
    public List<IngredientRecord> findAll() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(IngredientRecord.class, scanExpression);
    }

    @Override
    public void save(IngredientRecord ingredientRecord) {
        dynamoDBMapper.save(ingredientRecord);
    }

    @Override
    public void update(String id, IngredientRecord ingredientRecord) {
        dynamoDBMapper.save(ingredientRecord);
    }

    @Override
    public void delete(String id) {
        IngredientRecord ingredientRecord = new IngredientRecord();
        ingredientRecord.setId(id);
        dynamoDBMapper.delete(ingredientRecord);
    }
}
