package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarWashDto {

    @JsonProperty("user_id")
    private final int id;

    @JsonProperty("phone_number")
    private final String phoneNumber;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("picture")
    private final String picture;

    @JsonProperty("rating")
    private final double rating;

    @JsonProperty("address")
    private final String address;

    @JsonProperty("map_position")
    private final MapPositionDto mapPositionDto;

}
