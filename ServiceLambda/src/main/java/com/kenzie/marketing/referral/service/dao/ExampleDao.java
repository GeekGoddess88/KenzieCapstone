package com.kenzie.marketing.referral.service.dao;

import com.kenzie.marketing.referral.model.ExampleData;
import com.kenzie.marketing.referral.service.exceptions.InvalidDataException;
import com.kenzie.marketing.referral.service.model.ExampleRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class ExampleDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public ExampleDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public ExampleData storeExampleData(ExampleData exampleData) {
        try {
            mapper.save(exampleData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("id has already been used");
        }

        return exampleData;
    }

    public List<ExampleRecord> getExampleData(String id) {
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(id);

        DynamoDBQueryExpression<ExampleRecord> queryExpression = new DynamoDBQueryExpression<ExampleRecord>()
                .withHashKeyValues(exampleRecord)
                .withConsistentRead(false);

        return mapper.query(ExampleRecord.class, queryExpression);
    }
}
