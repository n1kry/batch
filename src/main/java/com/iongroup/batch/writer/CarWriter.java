package com.iongroup.batch.writer;


import com.iongroup.batch.model.Car;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//public class CarWriter implements ItemWriter<Car> {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public void write(Chunk<? extends Car> chunk) throws Exception {
//        String sql = "INSERT INTO car (model, maker, carnumber, madeyear) VALUES (?, ?, ?, ?)";
//        for (Car car : chunk) {
//            jdbcTemplate.update(sql, car.getModel(), car.getMaker(), car.getCarNumber(), car.getMadeYear());
//        }
//    }
//}

