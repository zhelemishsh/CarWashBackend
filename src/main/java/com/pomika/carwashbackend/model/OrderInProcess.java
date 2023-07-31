package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "orders_in_process")
public class OrderInProcess {
    public OrderInProcess(){}

    public OrderInProcess(Car car,
                          Date startTime){
        this.car = car;
        this.startTime = startTime;
    }

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "start_time")
    private Date startTime;
}
