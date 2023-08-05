package com.pomika.carwashbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchArea {
    public SearchArea(double latitude, double longitude, double radius){
        this.center = new MapPosition(latitude,longitude);
        this.radius = radius;
    }
    private MapPosition center;
    private double radius;
}
