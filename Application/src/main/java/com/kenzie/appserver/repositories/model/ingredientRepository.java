package com.kenzie.appserver.repositories.model;

import com.kenzie.capstone.service.model.IngredientRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ingredientRepository extends CrudRepository<IngredientRecord, String> {
}
