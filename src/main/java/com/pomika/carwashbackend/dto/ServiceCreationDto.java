package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.ServiceType;
import lombok.Getter;

@Getter
public class ServiceCreationDto {
    public ServiceCreationDto(
            @JsonProperty("car_type") CarType carType,
            @JsonProperty("service_type") ServiceType serviceType,
            @JsonProperty("wash_time") int washTime,
            @JsonProperty("price") int price
    ){
        this.carType = carType;
        this.serviceType = serviceType;
        this.washTime = washTime;
        this.price = price;
    }

    private final CarType carType;
    private final ServiceType serviceType;
    private final int washTime;
    private final int price;
}
