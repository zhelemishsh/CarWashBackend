package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.exception.AccountServiceException;
import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.repository.AccountRepository;
import com.pomika.carwashbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public void updatePhoneNumber(String phoneNumber, String newPhoneNumber)
            throws AccountServiceException{
        Account account = getAccountByPhoneNumber(phoneNumber);
        account.setPhoneNumber(newPhoneNumber);
        accountRepository.save(account);
    }

    @Override
    public void updatePassword(String phoneNumber, String newPassword)
            throws AccountServiceException{
        Account account = getAccountByPhoneNumber(phoneNumber);
        account.setPassword(newPassword); //TODO add encoder
        accountRepository.save(account);
    }

    private Account getAccountByPhoneNumber(String phoneNumber)
            throws AccountServiceException{
        Account account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account == null){
            throw new AccountServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return account;
    }
}
