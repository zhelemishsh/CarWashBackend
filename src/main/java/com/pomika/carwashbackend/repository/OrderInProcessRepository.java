package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.CarWash;
import com.pomika.carwashbackend.model.OrderInProcess;
import com.pomika.carwashbackend.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInProcessRepository extends JpaRepository<OrderInProcess, Integer> {
    OrderInProcess findByCar_UserAccount(UserAccount userAccount);

    List<OrderInProcess> findByCarWash(CarWash carWash);

    OrderInProcess findById(int orderId);
}
