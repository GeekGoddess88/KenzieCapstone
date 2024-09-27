package com.kenzie.capstone.service.task;

import com.kenzie.capstone.service.dao.IngredientDAO;
import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.concurrent.Callable;

public class IngredientTask implements Callable<Void> {

    private final IngredientDAO ingredientDao;
    private final IngredientRecord ingredientRecord;

    public IngredientTask(IngredientDAO ingredientDao, IngredientRecord ingredientRecord) {
        this.ingredientDao = ingredientDao;
        this.ingredientRecord = ingredientRecord;
    }

    @Override
    public Void call() throws Exception {
        ingredientDao.save(ingredientRecord);
        return null;
    }
}
