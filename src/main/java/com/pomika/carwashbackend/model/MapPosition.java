package com.pomika.carwashbackend.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class MapPosition {
    public MapPosition(){};

    private double latitude;
    private double longitude;
}
