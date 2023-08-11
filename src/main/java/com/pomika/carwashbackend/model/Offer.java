package com.pomika.carwashbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Offer {
    private final Date startTime;
    private final int price;
    private final int washTime;
}
