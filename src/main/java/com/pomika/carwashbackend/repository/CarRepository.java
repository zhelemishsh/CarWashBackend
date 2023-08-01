package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.Car;
import com.pomika.carwashbackend.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByUserAccount(UserAccount userAccount);
}
