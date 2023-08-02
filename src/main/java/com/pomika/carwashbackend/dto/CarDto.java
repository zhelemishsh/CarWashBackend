package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.CarType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarDto {

    @JsonProperty("id")
    private final int id;

    @JsonProperty("number")
    private final String number;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("type")
    private final CarType type;
}
