package com.iongroup.batch.processor;

import com.iongroup.batch.model.Car;
import com.iongroup.batch.model.dto.CarDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CarValidationProcessor implements ItemProcessor<CarDTO, Car> {

    @Override
    public Car process(CarDTO item) throws Exception {
        // Валидация данных и создание объекта Car
        Car car = new Car();
        car.setModel(item.getModel());
        car.setMaker(item.getMaker());
        car.setCarNumber(item.getCarNumber());
        car.setMadeYear(item.getMadeYear());

        return car;
    }
}
