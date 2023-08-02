package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByPhoneNumber(String phoneNumber);
}
