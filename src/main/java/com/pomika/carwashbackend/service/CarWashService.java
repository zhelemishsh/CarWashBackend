package com.pomika.carwashbackend.service;

import com.pomika.carwashbackend.dto.*;

import java.util.List;

public interface CarWashService {
    void createCarWash(CarWashCreationDto carWashCreationDto);
    void updateProfile(String ownerPhoneNumber, CarWashUpdateDto carWashUpdateDto);
    void deleteCarWash(String ownerPhoneNumber);
    void addService(String ownerPhoneNumber, ServiceCreationDto serviceCreationDto);
    void deleteService(String ownerPhoneNumber, int serviceId);
    List<ServiceDto> getServices(String ownerPhoneNumber);
    CarWashDto getCarWash(String PhoneNumber);
}
