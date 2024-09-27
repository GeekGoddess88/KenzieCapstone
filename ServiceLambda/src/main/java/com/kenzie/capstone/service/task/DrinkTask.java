package com.kenzie.capstone.service.task;

import com.kenzie.capstone.service.dao.DrinkDAO;
import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.concurrent.Callable;

public class DrinkTask implements Callable<Void> {

    private final DrinkDAO drinkDao;
    private final DrinkRecord drinkRecord;

    public DrinkTask(DrinkDAO drinkDao, DrinkRecord drinkRecord) {
        this.drinkDao = drinkDao;
        this.drinkRecord = drinkRecord;
    }

    @Override
    public Void call() throws Exception {
        drinkDao.save(drinkRecord);
        return null;
    }

}
