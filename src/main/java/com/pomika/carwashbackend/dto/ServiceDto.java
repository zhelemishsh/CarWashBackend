package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceDto {

    @JsonProperty("id")
    private final int id;
    @JsonProperty("car_type")
    private final CarType carType;
    @JsonProperty("service_type")
    private final ServiceType serviceType;
    @JsonProperty("wash_time")
    private final int washTime;
    @JsonProperty("price")
    private final int price;

}
