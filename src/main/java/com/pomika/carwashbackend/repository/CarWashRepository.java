package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.model.CarWash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarWashRepository extends JpaRepository<CarWash, Integer> {
    CarWash findByAccount(Account account);
}
