package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class OfferDto {
    @JsonProperty("id")
    private final int id;

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

    @JsonProperty("latitude")
    private final double latitude;

    @JsonProperty("longitude")
    private final double longitude;

    @JsonProperty("car_wash_name")
    private final String carWashName;
}
