package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.dto.*;
import com.pomika.carwashbackend.exception.OrderSessionServiceException;
import com.pomika.carwashbackend.model.*;
import com.pomika.carwashbackend.repository.*;
import com.pomika.carwashbackend.service.OrderSessionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;



@Service
public class OrderSessionServiceImpl implements OrderSessionService {
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final CarWashRepository carWashRepository;
    private final WashServiceRepository washServiceRepository;
    private final OrderInProcessRepository orderInProcessRepository;
    private final Timer timer;
    private final Map<Integer, Order> sessions;

    @Autowired
    public OrderSessionServiceImpl(AccountRepository accountRepository,
                                   UserAccountRepository userAccountRepository,
                                   CarWashRepository carWashRepository,
                                   WashServiceRepository washServiceRepository,
                                   OrderInProcessRepository orderInProcessRepository){
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.carWashRepository = carWashRepository;
        this.washServiceRepository = washServiceRepository;
        this.orderInProcessRepository = orderInProcessRepository;
        this.timer = new Timer(true);
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public void openSession(String userPhoneNumber, OrderSessionCreationDto orderSessionCreationDto)
            throws OrderSessionServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);

        if (sessions.get(userAccount.getId()) != null){
            throw new OrderSessionServiceException("Session already opened by user with phone number "
                    + userPhoneNumber);
        }

        Map<String,CarWashServicesInOrder> services = getCarWashesByOffer(orderSessionCreationDto);
        if (services.isEmpty()){
            throw new OrderSessionServiceException("There are no car washes with that parameters");
        }

        Order order = new Order(
                userAccount.getId(),
                orderSessionCreationDto.getStartTime(),
                orderSessionCreationDto.getEndTime(),
                new Car(orderSessionCreationDto.getCar().getId(),
                        userAccount,
                        orderSessionCreationDto.getCar().getNumber(),
                        orderSessionCreationDto.getCar().getName(),
                        orderSessionCreationDto.getCar().getType()),
                services
        );

        sessions.put(userAccount.getId(), order);

        // TODO send notification to all Car washes

        EndSessionTask endSessionTask = new EndSessionTask(sessions,userAccount.getId());
        timer.schedule(endSessionTask,120000);
    }

    @Override
    public void acceptOffer(String userPhoneNumber, String carWashPhoneNumber)
            throws OrderSessionServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);
        Order order = getOrderById(userAccount.getId());
        Offer acceptedOffer = order.getOffers().get(carWashPhoneNumber);

        if (acceptedOffer == null){
            throw new OrderSessionServiceException("Offer from car wash with number "
                    + carWashPhoneNumber + " doesnt exists");
        }

        CarWashServicesInOrder carWashServicesInOrder = order.getCarWashAndServices().get(carWashPhoneNumber);

        OrderInProcess orderInProcess = new OrderInProcess(
                order.getCar(),
                acceptedOffer.getStartTime(),
                carWashServicesInOrder.getCarWash(),
                carWashServicesInOrder.getServices()
        );

        orderInProcessRepository.save(orderInProcess);
        sessions.remove(userAccount.getId());
    }

    @Override
    public void createOffer(String carWashPhoneNumber, OfferCreationDto offerCreationDto, int orderId)
            throws OrderSessionServiceException{
        Order order = getOrderById(orderId);

        CarWashServicesInOrder carWashServices = order.getCarWashAndServices().get(carWashPhoneNumber);

        if(carWashServices == null){
            throw new OrderSessionServiceException("Car wash with number " + carWashPhoneNumber +
                    " dont participate in order with id " + orderId);
        }

        if(order.getOffers().get(carWashPhoneNumber) != null){
            throw new OrderSessionServiceException("Car wash with number " + carWashPhoneNumber +
                    " already created offer to order with id " + orderId);
        }

        if (offerCreationDto.getStartTime().before(order.getStartTime()) ||
                order.getEndTime().before(offerCreationDto.getStartTime())
        ){
            throw new OrderSessionServiceException("Offer time doesnt match order time interval");
        }

        Offer offer = new Offer(
                offerCreationDto.getStartTime(),
                carWashServices.getServices().stream().mapToInt(WashService::getPrice).sum(),
                carWashServices.getServices().stream().mapToInt(WashService::getWashTime).sum()
        );


        order.getOffers().put(carWashPhoneNumber,offer);

        if(order.getBestRating() < carWashServices.getCarWash().getRating()){
            order.setBestRating(carWashServices.getCarWash().getRating());
        }

        if(order.getBestPrice() > offer.getPrice()){
            order.setBestPrice(offer.getPrice());
        }
    }

    @Override
    public void declineOrder(String carWashPhoneNumber, int orderId)
            throws OrderSessionServiceException{
        Order order = getOrderById(orderId);

        if (order.getOffers().get(carWashPhoneNumber) != null){
            throw new OrderSessionServiceException("Can not decline order with id "
                    + orderId + " because of sent offer");
        }

        order.getCarWashAndServices().remove(carWashPhoneNumber);
    }

    @Override
    public List<OfferDto> getOffers(String userPhoneNumber)
            throws OrderSessionServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);
        Order order = getOrderById(userAccount.getId());
        return order.getOffers().entrySet().stream().map(
                o -> offerAndCarWashEntityToDto(
                        o.getValue(),
                        order.getCarWashAndServices().get(o.getKey()).getCarWash()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrders(String carWashPhoneNumber)
            throws OrderSessionServiceException{

        List<OrderDto> orders = new ArrayList<>();

        for (Order order : sessions.values()) {
            if (order.getCarWashAndServices().get(carWashPhoneNumber) != null &&
                    order.getOffers().get(carWashPhoneNumber) == null
            ){
                orders.add(orderEntityToDto(
                        order,
                        order.getCarWashAndServices().get(carWashPhoneNumber).getServices())
                );
            }
        }
        return orders;
    }

    @Override
    public OrderDto getOrder(String carWashPhoneNumber, int orderId) {
        Order order = getOrderById(orderId);

        CarWashServicesInOrder carWashAndServices = order.getCarWashAndServices().get(carWashPhoneNumber);

        if(carWashAndServices == null){
            throw new OrderSessionServiceException("Car wash with number " + carWashPhoneNumber +
                    " dont participate in order with id " + orderId);
        }

        return orderEntityToDto(order,carWashAndServices.getServices());
    }

    private Account findAccountByPhoneNumber(String phoneNumber){
        Account account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account == null){
            throw new OrderSessionServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return account;
    }

    private UserAccount findUserByPhoneNumber(String phoneNumber){
        Account account = findAccountByPhoneNumber(phoneNumber);
        UserAccount userAccount =  userAccountRepository.findByAccount(account);
        if (userAccount == null){
            throw new OrderSessionServiceException("Account with phone number "
                    + phoneNumber + "does not exists");
        }
        return userAccount;
    }


    private Map<String,CarWashServicesInOrder> getCarWashesByOffer(OrderSessionCreationDto orderSessionCreationDto){
        List<CarWash> carWashes = carWashRepository.findCarWashesInSquare(
                orderSessionCreationDto.getSearchArea().getCenter().getLatitude(),
                orderSessionCreationDto.getSearchArea().getCenter().getLongitude(),
                orderSessionCreationDto.getSearchArea().getRadius());

        return carWashes.stream()
                .filter(
                        o -> checkCarWashCoordinates(
                                o,
                                orderSessionCreationDto.getSearchArea().getCenter().getLatitude(),
                                orderSessionCreationDto.getSearchArea().getCenter().getLongitude(),
                                orderSessionCreationDto.getSearchArea().getRadius()
                        )
                )
                .map(
                        o -> getCarWashServicesByOrder(
                                o,
                                orderSessionCreationDto.getCar().getType(),
                                orderSessionCreationDto.getWashServiceTypes()
                        )
                )
                .filter(
                        o -> o.getServices().size() == orderSessionCreationDto.getWashServiceTypes().size()
                )
                .collect(
                        Collectors.toMap(
                                o -> o.getCarWash().getAccount().getPhoneNumber(),
                                o -> o,
                                (o1, o2) -> o1,
                                ConcurrentHashMap::new
                        )
                );
    }

    private boolean checkCarWashCoordinates(
            CarWash carWash,
            double centerLatitude,
            double centerLongitude,
            double radius
    ){
        double theta = centerLongitude - carWash.getLongitude();
        double distance = 60 * 1.1515 * (180/Math.PI) * Math.acos(
                Math.sin(centerLatitude * (Math.PI/180)) * Math.sin(carWash.getLatitude() * (Math.PI/180)) +
                Math.cos(centerLatitude * (Math.PI/180)) *
                Math.cos(carWash.getLatitude() * (Math.PI/180)) *
                Math.cos(theta * (Math.PI/180))
        );
        return radius > distance;
    }

    private CarWashServicesInOrder getCarWashServicesByOrder(
            CarWash carWash,
            CarType carType,
            List<WashServiceType> washServiceTypes
    ){
        List<WashService> washServices = washServiceRepository.findByCarWashAndCarTypeAndWashServiceTypeIn(
                carWash,
                carType,
                washServiceTypes
        );
        return new CarWashServicesInOrder(
                carWash,
                washServices
        );
    }


    private OrderDto orderEntityToDto(Order order, List<WashService> washServices){
        return new OrderDto(
                order.getUserId(),
                order.getBestPrice(),
                order.getBestRating(),
                order.getStartTime(),
                order.getEndTime(),
                order.getCar().getCarType(),
                order.getCar().getNumber(),
                order.getCar().getName(),
                washServices.stream().map(this::washServiceEntityToDto).collect(Collectors.toList())
        );
    }

    private WashServiceDto washServiceEntityToDto(WashService washService){
        return new WashServiceDto(
                washService.getId(),
                washService.getCarType(),
                washService.getWashServiceType(),
                washService.getWashTime(),
                washService.getPrice()
        );
    }

    private OfferDto offerAndCarWashEntityToDto(Offer offer, CarWash carWash){
        return new OfferDto(
                carWash.getAccount().getPhoneNumber(),
                offer.getStartTime(),
                offer.getPrice(),
                offer.getWashTime(),
                carWash.getRating(),
                carWash.getAddress(),
                carWash.getLatitude(),
                carWash.getLongitude(),
                carWash.getName()
        );
    }

    private Order getOrderById(int orderId){
        Order order = sessions.get(orderId);
        if(order == null){
            throw new OrderSessionServiceException("Order with id " + orderId + " does not exists");
        }
        return order;
    }
}

@AllArgsConstructor
class EndSessionTask extends TimerTask{
    private Map<Integer, Order> sessions;
    private int sessionId;
    @Override
    public void run(){
        sessions.remove(sessionId);
    }
}
