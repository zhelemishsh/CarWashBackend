package com.pomika.carwashbackend.service;

import com.pomika.carwashbackend.dto.*;

import java.util.List;

public interface OrderSessionService {
    void openSession(String userPhoneNumber, OrderSessionCreationDto orderSessionCreationDto);
    void acceptOffer(String userPhoneNumber, int offerId);
    void createOffer(String carWashPhoneNumber, OfferCreationDto offerCreationDto, int orderId);
    void declineOrder(String carWashPhoneNumber, int orderId);
    List<OfferDto> getOffers(String userPhoneNumber);
    List<OrderInListDto> getOrders(String carWashPhoneNumber);
    OrderDto getOrder(String carWashPhoneNumber, int orderId);
}
