package com.pomika.carwashbackend.exception;

public class OrderInProcessServiceException extends RuntimeException{
    public OrderInProcessServiceException(String message){
        super(message);
    }
}
