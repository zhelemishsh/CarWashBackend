package com.pomika.carwashbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class OrderOffer {
    private final int id;
    private final Date startTime;
    private final int price;
    private final int washTime;
    private final double carWashRating;
    private final String carWashAddress;
    private final MapPosition carWashLocation;
    private final String carWashName;
    private final String carWashPhoneNumber;
}
