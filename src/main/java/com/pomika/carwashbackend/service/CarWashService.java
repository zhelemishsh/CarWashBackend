package com.pomika.carwashbackend.service;

import com.pomika.carwashbackend.dto.*;

import java.util.List;

public interface CarWashService {
    void createCarWash(CarWashCreationDto carWashCreationDto);
    void updateProfile(String ownerPhoneNumber, CarWashUpdateDto carWashUpdateDto);
    void deleteCarWash(String ownerPhoneNumber);
    void addService(String ownerPhoneNumber, WashServiceCreationDto washServiceCreationDto);
    void deleteService(String ownerPhoneNumber, int serviceId);
    List<WashServiceDto> getServices(String ownerPhoneNumber);
    CarWashDto getCarWash(String PhoneNumber);
}
