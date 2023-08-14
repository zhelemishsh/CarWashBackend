package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.MapPosition;
import lombok.Getter;

@Getter
public class CarWashUpdateDto {
    public CarWashUpdateDto(
            @JsonProperty("name") String name,
            @JsonProperty("picture") String picture,
            @JsonProperty("address") String address,
            @JsonProperty("map_position")MapPositionDto mapPositionDto
    ){
        this.name= name;
        this.picture = picture;
        this.address = address;
        this.mapPosition = new MapPosition(
                mapPositionDto.getLatitude(),
                mapPositionDto.getLongitude()
        );
    }

    private final String name;
    private final String picture;
    private final String address;
    private final MapPosition mapPosition;
}
