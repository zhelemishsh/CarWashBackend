package com.pomika.carwashbackend.service;

import com.pomika.carwashbackend.dto.*;

import java.util.List;

public interface UserService {
    void createUser(UserCreationDto userCreationDto);
    void updateProfile(String userPhoneNumber, UserUpdateDto userUpdateDto);
    List<CarDto> getCars(String ownerPhoneNumber);
    void addCar(String ownerPhoneNumber, CarCreationDto carCreationDto);
    void deleteCar(String ownerPhoneNumber, int carId);
    UserDto getUser(String phoneNumber);
}
