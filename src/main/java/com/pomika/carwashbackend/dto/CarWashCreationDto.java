package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CarWashCreationDto {
    CarWashCreationDto(
            @JsonProperty("phone_number") String phoneNumber,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("picture") String picture,
            @JsonProperty("address") String address,
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude
    ){
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.picture = picture;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private final String phoneNumber;
    private final String password;
    private final String name;
    private final String picture;
    private final String address;
    private final double latitude;
    private final double longitude;
}
