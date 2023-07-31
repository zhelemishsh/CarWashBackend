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
                   String phone_number,
                   AccountRole accountRole){
        this.id = id;
        this.password = password;
        this.phone_number = phone_number;
        this.accountRole = accountRole;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "role")
    private AccountRole accountRole;
}
