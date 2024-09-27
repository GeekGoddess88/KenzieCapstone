package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.IngredientRecord;

import java.util.List;
import java.util.Optional;

public interface IngredientDAO {
    Optional<IngredientRecord> findById(String id);
    List<IngredientRecord> findAll();
    void save(IngredientRecord ingredient);
    void update(String id, IngredientRecord ingredient);
    void delete(String id);

}
