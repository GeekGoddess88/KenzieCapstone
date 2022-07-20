package com.kenzie.marketing.referral.service;

import com.kenzie.marketing.referral.model.ExampleData;
import com.kenzie.marketing.referral.service.dao.ExampleDao;
import com.kenzie.marketing.referral.service.model.ExampleRecord;

import javax.inject.Inject;

import java.util.List;

public class LambdaService {

    private ExampleDao exampleDao;

    @Inject
    public LambdaService(ExampleDao exampleDao) {
        this.exampleDao = exampleDao;
    }

    public ExampleData getExampleData(String id) {
        List<ExampleRecord> records = exampleDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }
}
