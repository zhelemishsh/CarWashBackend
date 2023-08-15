package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cars")
public class Car {
    public Car(){}

    public Car(int id,
               UserAccount userAccount,
               String number,
               String name,
               CarType carType
               ){
        this.id = id;
        this.userAccount = userAccount;
        this.number = number;
        this.name = name;
        this.carType = carType;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserAccount userAccount;

    @Column(name = "number")
    private String number;

    @Column(name = "name")
    private String name;

    @Column(name = "car_type")
    private CarType carType;
}
