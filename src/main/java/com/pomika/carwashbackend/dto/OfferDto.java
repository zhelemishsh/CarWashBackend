package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class OfferDto {
    @JsonProperty("car_wash_phone_number")
    private final String carWashPhoneNumber;

    @JsonProperty("start_time")
    private final Date startTime;

    @JsonProperty("price")
    private final int price;

    @JsonProperty("wash_time")
    private final int washTime;

    @JsonProperty("car_wash_rating")
    private final double carWashRating;

    @JsonProperty("car_wash_address")
    private final String carWashAddress;

    @JsonProperty("map_position")
    private final MapPositionDto mapPositionDto;

    @JsonProperty("car_wash_name")
    private final String carWashName;
}
