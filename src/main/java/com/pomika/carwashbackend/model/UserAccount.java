package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_accounts")
public class UserAccount {
    public UserAccount(){}

    public UserAccount(Account account,
                       String name,
                       byte[] picture){
        this.account = account;
        this.name = name;
        this.picture = picture;
    }

    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
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
}
