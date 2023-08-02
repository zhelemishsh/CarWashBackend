package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    UserAccount findByAccount(Account account);
}
