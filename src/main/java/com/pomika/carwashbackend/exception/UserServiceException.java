package com.pomika.carwashbackend.exception;

public class UserServiceException extends RuntimeException{
    public UserServiceException(String message){
        super(message);
    }
}
