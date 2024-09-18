package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.kenzie.capstone.service.model.DrinkRecord;
import com.kenzie.capstone.service.model.IngredientInterface;
import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.*;
/* Still fixing
just have a few methods to redo
 */


public class DrinkNonCachingDAO implements DrinkDAO {

    private final DynamoDBMapper dynamoDBMapper;

    public DrinkNonCachingDAO(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public DrinkRecord findById(String id) {
        return dynamoDBMapper.load(DrinkRecord.class, id);
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
        DrinkRecord drinkRecord = findById(id);
        if (drinkRecord != null) {
            dynamoDBMapper.delete(drinkRecord);
        }
    }

    @Override
    public List<DrinkRecord> findAll() {
        return dynamoDBMapper.scan(DrinkRecord.class, new DynamoDBScanExpression());
    }
}
