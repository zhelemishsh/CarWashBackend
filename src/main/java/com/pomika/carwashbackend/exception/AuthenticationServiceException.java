package com.pomika.carwashbackend.exception;

public class AuthenticationServiceException extends RuntimeException{
    public AuthenticationServiceException(String message){
        super(message);
    }
}
