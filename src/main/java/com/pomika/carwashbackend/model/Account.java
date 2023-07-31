package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {
    public Account(){}

    public Account(int id,
                   String password,
                   String phoneNumber,
                   AccountRole accountRole){
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.accountRole = accountRole;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    private AccountRole accountRole;
}
