package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.exception.AuthenticationServiceException;
import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
@Service
public class AuthenticationServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;
    @Autowired
    public AuthenticationServiceImpl(
            AccountRepository accountRepository
    ){
        this.accountRepository = accountRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Account account = findAccountByPhoneNumber(phoneNumber);
        return new User(
                account.getPhoneNumber(),
                account.getPassword(),
                getAuthoritiesByAccount(account)
        );
    }

    private Collection<GrantedAuthority> getAuthoritiesByAccount(Account account){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getAccountRole().toString()));
        return authorities;
    }

    private Account findAccountByPhoneNumber(String phoneNumber){
        Account account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account == null){
            throw new AuthenticationServiceException("Account with phone number "
                    + phoneNumber + " does not exists");
        }

        return account;
    }
}
