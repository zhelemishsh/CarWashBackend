package com.pomika.carwashbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CarWashServicesInOrder {
    private final CarWash carWash;
    private final List<WashService> services;
}
