package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import lombok.Getter;

@Getter
public class CarDto {
    public CarDto(
            String number,
            String name,
            CarType type,
            int id
    ){
        this.number = number;
        this.name = name;
        this.type = type;
        this.id = id;
    }

    @JsonProperty("id")
    private final int id;

    @JsonProperty("number")
    private final String number;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("type")
    private final CarType type;
}
