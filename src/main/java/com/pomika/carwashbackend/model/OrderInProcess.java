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
                          List<WashServiceType> washServiceTypes,
                          int price,
                          int washTime){
        this.car = car;
        this.startTime = startTime;
        this.washServiceTypes = washServiceTypes;
        this.carWash = carWash;
        this.price = price;
        this.washTime = washTime;
        this.isFinished = false;
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

    @Column(name = "wash_service_types")
    private List<WashServiceType> washServiceTypes;

    @Column(name = "price")
    private int price;

    @Column(name = "wash_time")
    private int washTime;

    @Column(name = "is_finished")
    private boolean isFinished;
}
