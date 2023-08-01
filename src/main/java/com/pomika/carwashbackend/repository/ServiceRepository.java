package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.CarWash;
import com.pomika.carwashbackend.model.Service;
import com.pomika.carwashbackend.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    void deleteByCarWash(CarWash carWash);

    List<Service> findByCarWash(CarWash carWash);
    Service findByCarWashAndCarTypeAndServiceType(CarWash carWash,
                                                  CarType carType,
                                                  ServiceType serviceType);
}
