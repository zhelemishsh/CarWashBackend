package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.MapPosition;
import lombok.Getter;

@Getter
public class CarWashCreationDto {
    CarWashCreationDto(
            @JsonProperty("phone_number") String phoneNumber,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("picture") String picture,
            @JsonProperty("address") String address,
            @JsonProperty("map_position")MapPositionDto mapPositionDto
            ){
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.picture = picture;
        this.address = address;
        this.mapPosition = new MapPosition(
                mapPositionDto.getLatitude(),
                mapPositionDto.getLongitude()
        );
    }

    private final String phoneNumber;
    private final String password;
    private final String name;
    private final String picture;
    private final String address;
    private final MapPosition mapPosition;
}
