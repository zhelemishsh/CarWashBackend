package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.OrderInProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInProcessRepository extends JpaRepository<OrderInProcess, Integer> {

}
