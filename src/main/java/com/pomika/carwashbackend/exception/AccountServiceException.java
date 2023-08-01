package com.pomika.carwashbackend.exception;

public class AccountServiceException extends RuntimeException{
    public AccountServiceException(String message){
        super(message);
    }
}
