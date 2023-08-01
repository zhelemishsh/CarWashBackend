package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CarWashDto {
    public CarWashDto(
            int id,
            String phoneNumber,
            String name,
            String picture,
            double rating,
            String address
    ){
        this.id = id;
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.rating = rating;
        this.address = address;
    }

    @JsonProperty("user_id")
    private final int id;

    @JsonProperty("phone_number")
    private final String phoneNumber;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("picture")
    private final String picture;

    @JsonProperty("rating")
    private final double rating;

    @JsonProperty("address")
    private final String address;
}
