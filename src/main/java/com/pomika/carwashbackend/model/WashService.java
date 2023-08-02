package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "services")
public class WashService {
    public WashService(){}

    public WashService(int id,
                       CarWash carWash,
                       CarType carType,
                       ServiceType serviceType,
                       int washTime,
                       int price
            ){
        this.id = id;
        this.carWash = carWash;
        this.carType = carType;
        this.serviceType = serviceType;
        this.washTime = washTime;
        this.price = price;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "car_wash_id")
    private CarWash carWash;

    @Column(name = "car_type")
    private CarType carType;

    @Column(name = "service_type")
    private ServiceType serviceType;

    @Column(name = "wash_time")
    private int washTime;

    @Column(name = "price")
    private int price;
}
