package com.kenzie.appserver.controller.model;

import com.kenzie.capstone.service.model.DrinkRecord;

import java.util.List;

public interface DrinkDAO {
    DrinkRecord findById(String id);
    List<DrinkRecord> findAll();
    void save(DrinkRecord drink);
    void update(String id, DrinkRecord drink);
    void delete(String id);
}

