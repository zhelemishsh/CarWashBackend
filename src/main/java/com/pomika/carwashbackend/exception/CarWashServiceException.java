package com.pomika.carwashbackend.exception;

public class CarWashServiceException extends RuntimeException{
    public CarWashServiceException(String message){
        super(message);
    }
}
