package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.dto.CarDto;
import com.pomika.carwashbackend.dto.MapPositionDto;
import com.pomika.carwashbackend.dto.OrderInProcessForCarWashDto;
import com.pomika.carwashbackend.dto.OrderInProcessForUserDto;
import com.pomika.carwashbackend.exception.CarWashServiceException;
import com.pomika.carwashbackend.exception.OrderInProcessServiceException;
import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.model.CarWash;
import com.pomika.carwashbackend.model.OrderInProcess;
import com.pomika.carwashbackend.model.UserAccount;
import com.pomika.carwashbackend.repository.AccountRepository;
import com.pomika.carwashbackend.repository.CarWashRepository;
import com.pomika.carwashbackend.repository.OrderInProcessRepository;
import com.pomika.carwashbackend.repository.UserAccountRepository;
import com.pomika.carwashbackend.service.OrderInProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInProcessServiceImpl implements OrderInProcessService {

    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final OrderInProcessRepository orderInProcessRepository;
    private final CarWashRepository carWashRepository;


    @Autowired
    public OrderInProcessServiceImpl (
            AccountRepository accountRepository,
            UserAccountRepository userAccountRepository,
            OrderInProcessRepository orderInProcessRepository,
            CarWashRepository carWashRepository
    ){
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.orderInProcessRepository = orderInProcessRepository;
        this.carWashRepository = carWashRepository;
    }


    @Override
    public OrderInProcessForUserDto getOrder(String userPhoneNumber)
            throws OrderInProcessServiceException{
        OrderInProcess orderInProcess = findOrderInProcessByUserPhoneNumber(userPhoneNumber);
        return orderInProcessEntityToUserDto(orderInProcess);
    }

    @Override
    public List<OrderInProcessForCarWashDto> getOrders(String carWashPhoneNumber)
            throws OrderInProcessServiceException{
        List<OrderInProcess> ordersInProcess = findOrdersInProcessByCarWashPhoneNumber(carWashPhoneNumber);
        return ordersInProcess.stream()
                .map(this::orderInProcessEntityToCarWashDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrderByUser(String userPhoneNumber)
            throws OrderInProcessServiceException{
        OrderInProcess orderInProcess = findOrderInProcessByUserPhoneNumber(userPhoneNumber);
        if (orderInProcess.isFinished()){
            throw new OrderInProcessServiceException("Can not cancel order that already finished and not rated");
        }
        orderInProcessRepository.delete(orderInProcess);
        //TODO notify car wash about canceled order
    }

    @Override
    public void cancelOrderByCarWash(String carWashPhoneNumber, int orderInProcessId)
            throws OrderInProcessServiceException{
        CarWash carWash = findCarWashByPhoneNumber(carWashPhoneNumber);
        OrderInProcess orderInProcess = findOrderInProcessByOrderId(orderInProcessId);
        if (!orderInProcess.getCarWash().equals(carWash)){
            throw new OrderInProcessServiceException("Car wash with phone number " +
                    carWashPhoneNumber + " does not participate in order with id " +
                    orderInProcessId);
        }

        if (orderInProcess.isFinished()){
            throw new OrderInProcessServiceException("Can not cancel order that already finished until user rate");
        }

        orderInProcessRepository.delete(orderInProcess);
        //TODO notify user about canceled order
    }

    @Override
    public void finishOrder(String carWashPhoneNumber, int orderInProcessId, double orderRating)
            throws OrderInProcessServiceException{
        CarWash carWash = findCarWashByPhoneNumber(carWashPhoneNumber);
        OrderInProcess orderInProcess = findOrderInProcessByOrderId(orderInProcessId);
        if (!orderInProcess.getCarWash().equals(carWash)){
            throw new OrderInProcessServiceException("Car wash with phone number " +
                    carWashPhoneNumber + " does not participate in order with id " +
                    orderInProcessId);
        }

        if (orderInProcess.isFinished()){
            throw new OrderInProcessServiceException("Can not finish that already finished");
        }

        orderInProcess.setFinished(true);
        orderInProcessRepository.save(orderInProcess);
        //TODO somehow rate user
    }

    @Override
    public void rateFinishedOrder(String userPhoneNumber, double orderRating)
            throws OrderInProcessServiceException{
        OrderInProcess orderInProcess = findOrderInProcessByUserPhoneNumber(userPhoneNumber);
        if (!orderInProcess.isFinished()){
            throw new OrderInProcessServiceException("Can not rate car wash because order not yet finished");
        }

        //TODO somehow rate car wash
        orderInProcessRepository.delete(orderInProcess);
    }

    private Account findAccountByPhoneNumber(String phoneNumber){
        Account account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account == null){
            throw new OrderInProcessServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return account;
    }

    private UserAccount findUserByPhoneNumber(String phoneNumber){
        Account account = findAccountByPhoneNumber(phoneNumber);
        UserAccount userAccount =  userAccountRepository.findByAccount(account);
        if (userAccount == null){
            throw new OrderInProcessServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return userAccount;
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

    private OrderInProcess findOrderInProcessByUserPhoneNumber(String phoneNumber){
        UserAccount userAccount = findUserByPhoneNumber(phoneNumber);
        OrderInProcess orderInProcess = orderInProcessRepository.findByCar_UserAccount(userAccount);
        if (orderInProcess == null){
            throw new OrderInProcessServiceException("Order in process for user with phone number "
                    + phoneNumber + " does not exists");
        }
        return orderInProcess;
    }

    private  List<OrderInProcess> findOrdersInProcessByCarWashPhoneNumber(String phoneNumber){
        CarWash carWash = findCarWashByPhoneNumber(phoneNumber);
        List<OrderInProcess> ordersInProcess = orderInProcessRepository.findByCarWash(carWash);
        if (ordersInProcess.isEmpty()){
            throw new OrderInProcessServiceException("Orders in process for car wash wth number "
            + phoneNumber + " does not exist");
        }
        return  ordersInProcess;
    }

    private  OrderInProcess findOrderInProcessByOrderId(int orderInProcessId){
        OrderInProcess orderInProcess = orderInProcessRepository.findById(orderInProcessId);
        if (orderInProcess == null){
            throw new OrderInProcessServiceException("Order in process with id "
                    + orderInProcessId + " does not exists");
        }
        return  orderInProcess;
    }

    private OrderInProcessForUserDto orderInProcessEntityToUserDto(OrderInProcess orderInProcess){
        return new OrderInProcessForUserDto(
                orderInProcess.getId(),
                orderInProcess.getStartTime(),
                orderInProcess.getWashTime(),
                orderInProcess.getPrice(),
                new CarDto(
                        orderInProcess.getCar().getId(),
                        orderInProcess.getCar().getNumber(),
                        orderInProcess.getCar().getName(),
                        orderInProcess.getCar().getCarType()
                ),
                orderInProcess.getWashServiceTypes(),
                orderInProcess.getCarWash().getName(),
                orderInProcess.getCarWash().getAddress(),
                new MapPositionDto(
                        orderInProcess.getCarWash().getMapPosition().getLatitude(),
                        orderInProcess.getCarWash().getMapPosition().getLongitude()
                ),
                orderInProcess.getCarWash().getPicture(),
                orderInProcess.getCarWash().getAccount().getPhoneNumber()
        );
    }

    private OrderInProcessForCarWashDto orderInProcessEntityToCarWashDto(OrderInProcess orderInProcess){
        return new OrderInProcessForCarWashDto(
                orderInProcess.getId(),
                orderInProcess.getStartTime(),
                orderInProcess.getWashTime(),
                orderInProcess.getPrice(),
                new CarDto(
                        orderInProcess.getCar().getId(),
                        orderInProcess.getCar().getNumber(),
                        orderInProcess.getCar().getName(),
                        orderInProcess.getCar().getCarType()
                ),
                orderInProcess.getWashServiceTypes(),
                orderInProcess.getCar().getUserAccount().getAccount().getPhoneNumber()
        );
    }
}
