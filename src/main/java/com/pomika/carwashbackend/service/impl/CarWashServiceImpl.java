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
import com.pomika.carwashbackend.repository.ServiceRepository;
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
    private final ServiceRepository serviceRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public CarWashServiceImpl(CarWashRepository carWashRepository,
                              ServiceRepository serviceRepository,
                              AccountRepository accountRepository){
        this.carWashRepository = carWashRepository;
        this.serviceRepository = serviceRepository;
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
                carWashCreationDto.getLatitude(),
                carWashCreationDto.getLongitude()));
    }

    @Override
    public void updateProfile(String ownerPhoneNumber, CarWashUpdateDto carWashUpdateDto)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        carWash.setName(carWashUpdateDto.getName());
        carWash.setAddress(carWashUpdateDto.getAddress());
        carWash.setPicture(carWashUpdateDto.getPicture());
        carWash.setLatitude(carWashUpdateDto.getLatitude());
        carWash.setLongitude(carWashUpdateDto.getLongitude());
        carWashRepository.save(carWash);
    }

    @Transactional
    @Override
    public void deleteCarWash(String ownerPhoneNumber) //TODO deleting car wash may decline service order
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        serviceRepository.deleteByCarWash(carWash);
        carWashRepository.delete(carWash);
        accountRepository.delete(carWash.getAccount());
    }

    @Override
    public void addService(String ownerPhoneNumber, ServiceCreationDto serviceCreationDto)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        WashService washService = serviceRepository.findByCarWashAndCarTypeAndServiceType(
                carWash,
                serviceCreationDto.getCarType(),
                serviceCreationDto.getServiceType()
        );

        if (washService != null){
            throw new CarWashServiceException("Service " + serviceCreationDto.getServiceType() +
                    " with car type " + serviceCreationDto.getCarType() + " already exists");
        }

        serviceRepository.save(new WashService(
                0,
                carWash,
                serviceCreationDto.getCarType(),
                serviceCreationDto.getServiceType(),
                serviceCreationDto.getWashTime(),
                serviceCreationDto.getPrice()
        ));
    }

    @Override
    public void deleteService(String ownerPhoneNumber, int serviceId)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        Optional<WashService> service = serviceRepository.findById(serviceId);

        if (service.isEmpty()){
            throw new CarWashServiceException("Service with id " + serviceId + " does not exists");
        }

        if (!service.get().getCarWash().equals(carWash)){
            throw new CarWashServiceException("Car wash with phone number " + ownerPhoneNumber +
                    " does not own service with id " + serviceId);
        }

        serviceRepository.delete(service.get());
    }

    @Override
    public List<ServiceDto> getServices(String ownerPhoneNumber)
            throws CarWashServiceException{
        CarWash carWash = findCarWashByPhoneNumber(ownerPhoneNumber);
        List<WashService> washServices = serviceRepository.findByCarWash(carWash);
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
                carWash.getLatitude(),
                carWash.getLongitude());
    }

    private ServiceDto serviceEntityToDto(WashService washService){
        return new ServiceDto(
                washService.getId(),
                washService.getCarType(),
                washService.getServiceType(),
                washService.getWashTime(),
                washService.getPrice());
    }
}
