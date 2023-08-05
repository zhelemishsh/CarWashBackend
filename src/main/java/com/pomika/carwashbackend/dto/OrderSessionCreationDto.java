package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomika.carwashbackend.model.SearchArea;
import com.pomika.carwashbackend.model.WashServiceType;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class OrderSessionCreationDto {
    public OrderSessionCreationDto(
            @JsonProperty("startTime") Date startTime,
            @JsonProperty("endTime") Date endTime,
            @JsonProperty("car") CarDto car,
            @JsonProperty("wash_services") List<WashServiceType> washServiceTypes,
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude,
            @JsonProperty("radius") double radius
    ){
        this.startTime = startTime;
        this.endTime = endTime;
        this.car = car;
        this.washServiceTypes = washServiceTypes;
        this.searchArea = new SearchArea(latitude,longitude,radius);
    }

    private final Date startTime;
    private final Date endTime;
    private final CarDto car;
    private final List<WashServiceType> washServiceTypes;
    private final SearchArea searchArea;
}
