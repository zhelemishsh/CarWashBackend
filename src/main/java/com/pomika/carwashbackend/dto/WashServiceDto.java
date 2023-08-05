package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.WashServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WashServiceDto {

    @JsonProperty("id")
    private final int id;
    @JsonProperty("car_type")
    private final CarType carType;
    @JsonProperty("service_type")
    private final WashServiceType washServiceType;
    @JsonProperty("wash_time")
    private final int washTime;
    @JsonProperty("price")
    private final int price;

}
