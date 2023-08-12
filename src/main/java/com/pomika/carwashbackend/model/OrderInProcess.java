package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders_in_process")
public class OrderInProcess {
    public OrderInProcess(){}

    public OrderInProcess(Car car,
                          Date startTime,
                          CarWash carWash,
                          List<WashService> washServices){
        this.car = car;
        this.startTime = startTime;
        this.washServices = washServices;
        this.carWash = carWash;
    }

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY) //TODO check LAZY
    @MapsId
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "start_time")
    private Date startTime;

    @ManyToOne
    @JoinColumn(name = "car_wash_id")
    private CarWash carWash;

    @ManyToMany
    private List<WashService> washServices;

}
