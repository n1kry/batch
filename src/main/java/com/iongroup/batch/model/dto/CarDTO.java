package com.iongroup.batch.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CarDTO {
    private String model;

    private String maker;

    private String carNumber;

    private Integer madeYear;

    private String startedRentOn;

    private String finishedRentOn;
}
