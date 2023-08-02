package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserCreationDto {
    public UserCreationDto(
            @JsonProperty("phone_number") String phoneNumber,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("picture") String picture
    ){
        this.name= name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
    }

    private final String phoneNumber;
    private final String password;
    private final String name;
    private final String picture;
}
