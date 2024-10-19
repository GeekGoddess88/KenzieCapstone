package com.kenzie.appserver.repositories;

import com.kenzie.capstone.service.model.DrinkRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface DrinkRepository extends CrudRepository<DrinkRecord, String> {
}
