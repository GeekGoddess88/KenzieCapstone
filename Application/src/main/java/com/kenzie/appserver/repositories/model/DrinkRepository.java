package com.kenzie.appserver.repositories.model;

import com.kenzie.capstone.service.model.DrinkRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface DrinkRepository extends CrudRepository<DrinkRecord, String> {
}
