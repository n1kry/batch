package com.iongroup.batch.dao;

import com.iongroup.batch.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDAO extends JpaRepository<Car, Integer> {
}
