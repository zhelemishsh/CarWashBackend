package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "car_washes")
public class CarWash {
    public CarWash(){}

    public CarWash(Account account,
            String name,
            double rating,
            byte[] picture,
            String address,
            double latitude,
            double longitude
            ){
        this.account = account;
        this.name = name;
        this.rating = rating;
        this.picture = picture;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY) //TODO check LAZY
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "name")
    private String name;

    @Column(name = "rating")
    private double rating;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;


}
