package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class OfferCreationDto {

    public OfferCreationDto(
            @JsonProperty("start_time") Date startTime
    ){
        this.startTime = startTime;
    }

    private final Date startTime;
}
