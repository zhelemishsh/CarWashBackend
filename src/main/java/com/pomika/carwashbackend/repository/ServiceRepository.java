package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.CarWash;
import com.pomika.carwashbackend.model.WashService;
import com.pomika.carwashbackend.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<WashService, Integer> {
    void deleteByCarWash(CarWash carWash);

    List<WashService> findByCarWash(CarWash carWash);
    WashService findByCarWashAndCarTypeAndServiceType(CarWash carWash,
                                                      CarType carType,
                                                      ServiceType serviceType);
}
