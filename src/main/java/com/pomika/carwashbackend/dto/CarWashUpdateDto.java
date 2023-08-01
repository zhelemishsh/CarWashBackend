package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CarWashUpdateDto {
    public CarWashUpdateDto(
            @JsonProperty("name") String name,
            @JsonProperty("picture") String picture,
            @JsonProperty("address") String address,
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude
    ){
        this.name= name;
        this.picture = picture;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private final String name;
    private final String picture;
    private final String address;
    private final double latitude;
    private final double longitude;
}
