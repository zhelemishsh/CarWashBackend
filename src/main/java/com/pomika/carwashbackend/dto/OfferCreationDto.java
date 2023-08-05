package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class OfferCreationDto {

    public OfferCreationDto(
            @JsonProperty("start_time") Date startTime,
            @JsonProperty("full_price") int fullPrice,
            @JsonProperty("wash_time") int washTime
    ){
        this.fullPrice = fullPrice;
        this.startTime = startTime;
        this.washTime = washTime;
    }

    private final Date startTime;
    private final int fullPrice;
    private final int washTime;
}
