package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.dto.*;
import com.pomika.carwashbackend.exception.UserServiceException;
import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.model.AccountRole;
import com.pomika.carwashbackend.model.Car;
import com.pomika.carwashbackend.model.UserAccount;
import com.pomika.carwashbackend.repository.AccountRepository;
import com.pomika.carwashbackend.repository.CarRepository;
import com.pomika.carwashbackend.repository.UserAccountRepository;
import com.pomika.carwashbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;
    private final CarRepository carRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public  UserServiceImpl(UserAccountRepository userAccountRepository,
                            CarRepository carRepository,
                            AccountRepository accountRepository){
        this.userAccountRepository = userAccountRepository;
        this.carRepository = carRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void createUser(UserCreationDto userCreationDto)
            throws UserServiceException{
        Account account = accountRepository.findByPhoneNumber(userCreationDto.getPhoneNumber());
        if(account != null){
            throw new UserServiceException("Account with phone number "
                    + userCreationDto.getPhoneNumber() + "already exists");
        }

        account = accountRepository.save(new Account(
                0,
                userCreationDto.getPassword(),
                userCreationDto.getPhoneNumber(),
                AccountRole.ROLE_USER));

        userAccountRepository.save(new UserAccount(
                account,
                userCreationDto.getName(),
                5.0,                        //TODO user rating
                userCreationDto.getPicture()));
    }

    @Override
    public void updateProfile(String userPhoneNumber, UserUpdateDto userUpdateDto)
            throws UserServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);
        userAccount.setName(userUpdateDto.getName());
        userAccount.setPicture(userUpdateDto.getPicture());
        userAccountRepository.save(userAccount);
    }

    @Override
    public List<CarDto> getCars(String ownerPhoneNumber)
            throws UserServiceException{
        UserAccount userAccount = findUserByPhoneNumber(ownerPhoneNumber);
        List<Car> cars = carRepository.findByUserAccount(userAccount);
        return cars.stream().map(this::carEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void addCar(String ownerPhoneNumber, CarCreationDto carCreationDto)
            throws UserServiceException{
        UserAccount userAccount = findUserByPhoneNumber(ownerPhoneNumber);
        carRepository.save(new Car(
                0,
                userAccount,
                carCreationDto.getNumber(),
                carCreationDto.getName(),
                carCreationDto.getType()));
    }

    @Override
    public void deleteCar(String ownerPhoneNumber, int carId) //TODO deleting cars mad decline service order
            throws UserServiceException{
        Optional<Car> car = carRepository.findById(carId);
        if(car.isEmpty()){
            throw new UserServiceException("Car with id "
                    + carId + "does not exists");
        }
        UserAccount userAccount = findUserByPhoneNumber(ownerPhoneNumber);

        if(!car.get().getUserAccount().equals(userAccount)){
            throw new UserServiceException("User with phone number "
                    + ownerPhoneNumber + "does not own car with id " + carId);
        }

        carRepository.delete(car.get());
    }

    @Override
    public UserDto getUser(String phoneNumber)
            throws UserServiceException{
        UserAccount userAccount = findUserByPhoneNumber(phoneNumber);
        return userEntityToDto(userAccount);
    }

    private Account findAccountByPhoneNumber(String phoneNumber){
        Account account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account == null){
            throw new UserServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return account;
    }

    private UserAccount findUserByPhoneNumber(String phoneNumber){
        Account account = findAccountByPhoneNumber(phoneNumber);
        UserAccount userAccount =  userAccountRepository.findByAccount(account);
        if (userAccount == null){
            throw new UserServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return userAccount;
    }

    private UserDto userEntityToDto(UserAccount userAccount){
        return new UserDto(
                userAccount.getId(),
                userAccount.getAccount().getPhoneNumber(),
                userAccount.getName(),
                userAccount.getPicture(),
                userAccount.getRating());
    }

    private CarDto carEntityToDto(Car car){
        return new CarDto(
                car.getId(),
                car.getNumber(),
                car.getName(),
                car.getCarType());
    }
}
