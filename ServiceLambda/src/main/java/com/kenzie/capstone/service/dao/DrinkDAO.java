package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.Drink;
import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.List;
import java.util.Optional;

public interface DrinkDAO {
    Optional<DrinkRecord> findById(String id);
    List<DrinkRecord> findAll();
    void save(DrinkRecord drink);
    void update(String id, DrinkRecord drink);
    void delete(String id);
}
