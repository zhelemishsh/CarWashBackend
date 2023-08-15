package com.pomika.carwashbackend.service;

import com.pomika.carwashbackend.dto.OrderInProcessForCarWashDto;
import com.pomika.carwashbackend.dto.OrderInProcessForUserDto;

import java.util.List;

public interface OrderInProcessService {
    OrderInProcessForUserDto getOrder(String userPhoneNumber);
    List<OrderInProcessForCarWashDto> getOrders(String carWashPhoneNumber);
    void cancelOrderByUser(String userPhoneNumber);
    void cancelOrderByCarWash(String carWashPhoneNumber, int orderInProcessId);
    void finishOrder(String carWashPhoneNumber, int orderInProcessId, double orderRating);
    void rateFinishedOrder(String userPhoneNumber, double orderRating);
}
