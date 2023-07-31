package com.pomika.carwashbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_services")
public class OrderService {
    public OrderService(){}

    public OrderService(int id,
                        OrderInProcess order,
                        Service service){
        this.id = id;
        this.order = order;
        this.service = service;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderInProcess order;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}
