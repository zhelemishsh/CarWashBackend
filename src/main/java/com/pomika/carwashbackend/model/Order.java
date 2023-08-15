package com.pomika.carwashbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Order {

    private final Integer userId;
    private final Map<String,Offer> offers;
    private final Date startTime;
    private final Date endTime;
    private final Car car;
    private Map<String, CarWashServicesInOrder> carWashAndServices;
    private double bestRating;
    private int bestPrice;
    private List<WashServiceType> washServiceTypes;

    public Order(
            int userId,
            Date startTime,
            Date endTime,
            Car car,
            Map<String,CarWashServicesInOrder> carWashAndServices,
            List<WashServiceType> washServiceTypes
    ){
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.car = car;
        this.offers = new ConcurrentHashMap<>();
        this.bestPrice = 0;
        this.bestRating = 0;
        this.carWashAndServices = carWashAndServices;
        this.washServiceTypes = washServiceTypes;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }

        if(!(o instanceof Order)){
           return false;
        }

        return userId.equals(((Order) o).userId);
    }

    @Override
    public int hashCode(){
        return userId.hashCode();
    }


}

