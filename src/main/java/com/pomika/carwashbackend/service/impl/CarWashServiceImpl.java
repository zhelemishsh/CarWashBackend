package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.dto.*;
import com.pomika.carwashbackend.exception.CarWashServiceException;
import com.pomika.carwashbackend.exception.UserServiceException;
import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.model.AccountRole;
import com.pomika.carwashbackend.model.CarWash;
import com.pomika.carwashbackend.model.WashService;
import com.pomika.carwashbackend.repository.AccountRepository;
import com.pomika.carwashbackend.repository.CarWashRepository;
import com.pomika.carwashbackend.repository.WashServiceRepository;
import com.pomika.carwashbackend.service.CarWashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarWashServiceImpl implements CarWashService {
    private final CarWashRepository carWashRepository;
    private final WashServiceRepository washServiceRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public CarWashServiceImpl(CarWashRepository carWashRepository,
                              WashServiceRepository washServiceRepository,
                              AccountRepository accountRepository){
        this.carWashRepository = carWashRepository;
        this.washServiceRepository = washServiceRepository;
        this.accountRepository = accountRepository;
    }
    @Override
    public void createCarWash(CarWashCreationDto carWashCreationDto)
            throws CarWashServiceException{
        Account account = accountRepository.findByPhoneNumber(carWashCreationDto.getPhoneNumber());
        if(account != null){
            throw new UserServiceException("Account with phone number "
                    + carWashCreationDto.getPhoneNumber() + "already exists");
        }

        account = accountRepository.save(new Account(
                0,
                carWashCreationDto.getPassword(),
                carWashCreationDto.getPhoneNumber(),
                AccountRole.CARWASH));

        carWashRepository.save(new CarWash(
                account,
                carWashCreationDto.getName(),
                5.0, //TODO rating
                carWashCreationDto.getPicture(),
                carWashCreationDto.getAddress(),
                carWashCreationDto.getMapPosition()));
    }

    @Override
    public void updateProfile(String ownerPhoneNumber, CarWashUpdateDto carWashUpdateDto)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        carWash.setName(carWashUpdateDto.getName());
        carWash.setAddress(carWashUpdateDto.getAddress());
        carWash.setPicture(carWashUpdateDto.getPicture());
        carWash.setMapPosition(carWashUpdateDto.getMapPosition());
        carWashRepository.save(carWash);
    }

    @Transactional
    @Override
    public void deleteCarWash(String ownerPhoneNumber) //TODO deleting car wash may decline service order
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        washServiceRepository.deleteByCarWash(carWash);
        carWashRepository.delete(carWash);
        accountRepository.delete(carWash.getAccount());
    }

    @Override
    public void addService(String ownerPhoneNumber, WashServiceCreationDto washServiceCreationDto)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        WashService washService = washServiceRepository.findByCarWashAndCarTypeAndWashServiceType(
                carWash,
                washServiceCreationDto.getCarType(),
                washServiceCreationDto.getWashServiceType()
        );

        if (washService != null){
            throw new CarWashServiceException("Service " + washServiceCreationDto.getWashServiceType() +
                    " with car type " + washServiceCreationDto.getCarType() + " already exists");
        }

        washServiceRepository.save(new WashService(
                0,
                carWash,
                washServiceCreationDto.getCarType(),
                washServiceCreationDto.getWashServiceType(),
                washServiceCreationDto.getWashTime(),
                washServiceCreationDto.getPrice()
        ));
    }

    @Override
    public void deleteService(String ownerPhoneNumber, int serviceId)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        Optional<WashService> service = washServiceRepository.findById(serviceId);

        if (service.isEmpty()){
            throw new CarWashServiceException("Service with id " + serviceId + " does not exists");
        }

        if (!service.get().getCarWash().equals(carWash)){
            throw new CarWashServiceException("Car wash with phone number " + ownerPhoneNumber +
                    " does not own service with id " + serviceId);
        }

        washServiceRepository.delete(service.get());
    }

    @Override
    public List<WashServiceDto> getServices(String ownerPhoneNumber)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        List<WashService> washServices = washServiceRepository.findByCarWash(carWash);
        return washServices.stream().map(this::serviceEntityToDto).collect(Collectors.toList());
    }

    @Override
    public CarWashDto getCarWash(String phoneNumber)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(phoneNumber);
        return carWashEntityToDto(carWash);
    }

    private Account findAccountByPhoneNumber(String phoneNumber){
        Account account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account == null){
            throw new CarWashServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return account;
    }

    private CarWash findCarWashByPhoneNumber(String phoneNumber){
        Account account = findAccountByPhoneNumber(phoneNumber);
        CarWash carWash =  carWashRepository.findByAccount(account);
        if (carWash == null){
            throw new CarWashServiceException("Car wash with phone number "
                    + phoneNumber + "does not exists");
        }
        return carWash;
    }

    private CarWashDto carWashEntityToDto(CarWash carWash){
        return new CarWashDto(
                carWash.getId(),
                carWash.getAccount().getPhoneNumber(),
                carWash.getName(),
                carWash.getPicture(),
                carWash.getRating(),
                carWash.getAddress(),
                new MapPositionDto(
                        carWash.getMapPosition().getLatitude(),
                        carWash.getMapPosition().getLongitude()
                ));
    }

    private WashServiceDto serviceEntityToDto(WashService washService){
        return new WashServiceDto(
                washService.getId(),
                washService.getCarType(),
                washService.getWashServiceType(),
                washService.getWashTime(),
                washService.getPrice());
    }
}
