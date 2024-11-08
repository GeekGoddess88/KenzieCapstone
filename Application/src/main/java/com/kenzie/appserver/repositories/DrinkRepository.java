package com.kenzie.appserver.repositories;

import com.kenzie.capstone.service.model.DrinkRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
@Qualifier("drinkRepository")
public interface DrinkRepository extends CrudRepository<DrinkRecord, String> {
}
