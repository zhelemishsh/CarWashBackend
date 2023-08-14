package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchAreaDto {
    @JsonProperty("map_position")
    private MapPositionDto mapPositionDto;

    @JsonProperty("radius")
    private double radius;
}
