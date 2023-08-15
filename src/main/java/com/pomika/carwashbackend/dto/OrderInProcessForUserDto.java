package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.WashServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderInProcessForUserDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("start_time")
    private Date startTime;

    @JsonProperty("wash_time")
    private int washTime;

    @JsonProperty("price")
    private int price;

    @JsonProperty("car")
    private CarDto car;

    @JsonProperty("wash_service_types")
    private List<WashServiceType> washServiceTypes;

    @JsonProperty("car_wash_name")
    private String carWashName;

    @JsonProperty("car_wash_address")
    private String carWashAddress;

    @JsonProperty("map_position")
    private MapPositionDto mapPositionDto;

    @JsonProperty("car_wash_picture")
    private String carWashPicture;

    @JsonProperty("car_wash_phone_number")
    private String carWashPhoneNumber;
}
