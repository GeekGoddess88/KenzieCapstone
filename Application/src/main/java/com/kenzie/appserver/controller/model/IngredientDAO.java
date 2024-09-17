package com.kenzie.appserver.controller.model;

import com.kenzie.capstone.service.model.IngredientInterface;
import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.List;

public interface IngredientDAO {
    IngredientRecord findById(String id);
    List<IngredientRecord> findAll();
    void save(IngredientRecord ingredient);
    void update(String id, IngredientRecord ingredient);
    void delete(String id);
}
