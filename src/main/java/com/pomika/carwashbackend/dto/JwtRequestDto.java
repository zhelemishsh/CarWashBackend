package com.pomika.carwashbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class JwtRequestDto {
    public JwtRequestDto(
        @JsonProperty("phone_number") String phoneNumber,
        @JsonProperty("password") String password
    ){
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    private final String phoneNumber;
    private final String password;
}
