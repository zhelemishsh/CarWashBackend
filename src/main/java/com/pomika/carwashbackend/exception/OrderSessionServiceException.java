package com.pomika.carwashbackend.exception;

import com.pomika.carwashbackend.service.impl.OrderSessionServiceImpl;

public class OrderSessionServiceException extends RuntimeException{
    public OrderSessionServiceException(String message){
        super(message);
    }
}
