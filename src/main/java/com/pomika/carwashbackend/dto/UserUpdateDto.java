package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserUpdateDto {
    public UserUpdateDto(
            @JsonProperty("name") String name,
            @JsonProperty("picture") String picture
    ){
        this.name= name;
        this.picture = picture;
    }

    private final String name;
    private final String picture;
}
