package com.pomika.carwashbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderSession {

    private final Integer userId;
    private final List<WashServiceType> washServiceTypes;
    private final List<OrderOffer> offers;
    private final Date startTime;
    private final Date endTime;
    private final Car car;
    private final List<CarWash> carWashes;
    private double bestRating;
    private int bestPrice;

    public OrderSession(
            int userId,
            List<WashServiceType> washServiceTypes,
            Date startTime,
            Date endTime,
            Car car,
            List<CarWash> carWashes
    ){
        this.userId = userId;
        this.washServiceTypes = washServiceTypes;
        this.startTime = startTime;
        this.endTime = endTime;
        this.car = car;
        this.carWashes = carWashes;
        this.offers = new ArrayList<>();
        this.bestPrice = 0;
        this.bestRating = 0;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }

        if(!(o instanceof OrderSession)){
           return false;
        }

        return userId.equals(((OrderSession) o).userId);
    }

    @Override
    public int hashCode(){
        return userId.hashCode();
    }


}

