package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.WashServiceType;
import lombok.Getter;

@Getter
public class WashServiceCreationDto {
    public WashServiceCreationDto(
            @JsonProperty("car_type") CarType carType,
            @JsonProperty("service_type") WashServiceType washServiceType,
            @JsonProperty("wash_time") int washTime,
            @JsonProperty("price") int price
    ){
        this.carType = carType;
        this.washServiceType = washServiceType;
        this.washTime = washTime;
        this.price = price;
    }

    private final CarType carType;
    private final WashServiceType washServiceType;
    private final int washTime;
    private final int price;
}
