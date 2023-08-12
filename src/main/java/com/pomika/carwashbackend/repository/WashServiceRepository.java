package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.CarType;
import com.pomika.carwashbackend.model.CarWash;
import com.pomika.carwashbackend.model.WashService;
import com.pomika.carwashbackend.model.WashServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WashServiceRepository extends JpaRepository<WashService, Integer> {
    void deleteByCarWash(CarWash carWash);

    List<WashService> findByCarWash(CarWash carWash);
    WashService findByCarWashAndCarTypeAndWashServiceType(CarWash carWash,
                                                      CarType carType,
                                                      WashServiceType washServiceType);
    List<WashService> findByCarWashAndCarTypeAndWashServiceTypeIn(CarWash carWash,
                                                                  CarType carType,
                                                                  Collection<WashServiceType> washServiceTypes);
}
