package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import lombok.Getter;

@Getter
public class CarCreationDto {
    public CarCreationDto(
            @JsonProperty("number") String number,
            @JsonProperty("name") String name,
            @JsonProperty("type") CarType type
    ){
        this.number = number;
        this.name = name;
        this.type = type;
    }

    private final String number;
    private final String name;
    private final CarType type;
}
