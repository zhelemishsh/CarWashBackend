package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.WashServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDto {
    @JsonProperty("id")
    private final int id;

    @JsonProperty("best_price")
    private final int best_price;

    @JsonProperty("best_rating")
    private final double best_rating;

    @JsonProperty("start_time")
    private final Date startTime;

    @JsonProperty("end_time")
    private final Date endTime;

    @JsonProperty("car_type")
    private final CarType carType;

    @JsonProperty("car_number")
    private final String carNumber;

    @JsonProperty("car_name")
    private final String name;

    @JsonProperty("wash_services")
    private final List<WashServiceDto> washServices;
}
