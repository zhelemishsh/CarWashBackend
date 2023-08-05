package com.pomika.carwashbackend.service.impl;

import com.pomika.carwashbackend.dto.*;
import com.pomika.carwashbackend.exception.CarWashServiceException;
import com.pomika.carwashbackend.exception.OrderSessionServiceException;
import com.pomika.carwashbackend.model.*;
import com.pomika.carwashbackend.repository.*;
import com.pomika.carwashbackend.service.OrderSessionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderSessionServiceImpl implements OrderSessionService {
    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final CarWashRepository carWashRepository;
    private final WashServiceRepository washServiceRepository;
    private final OrderInProcessRepository orderInProcessRepository;
    private final Timer timer;
    private final Map<Integer,OrderSession> sessions;

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
        this.sessions = new HashMap<>();
    }

    @Override
    public void openSession(String userPhoneNumber, OrderSessionCreationDto orderSessionCreationDto)
            throws OrderSessionServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);

        if (sessions.get(userAccount.getId()) != null){
            throw new OrderSessionServiceException("Session already opened by user with phone number "
                    + userPhoneNumber);
        }

        List<CarWash> carWashes = getCarWashesByOffer(orderSessionCreationDto);
        if (carWashes.isEmpty()){
            throw new OrderSessionServiceException("There are no car washes with that parameters");
        }

        OrderSession orderSession = new OrderSession(
                userAccount.getId(),
                orderSessionCreationDto.getWashServiceTypes(),
                orderSessionCreationDto.getStartTime(),
                orderSessionCreationDto.getEndTime(),
                new Car(orderSessionCreationDto.getCar().getId(),
                        userAccount,
                        orderSessionCreationDto.getCar().getNumber(),
                        orderSessionCreationDto.getCar().getName(),
                        orderSessionCreationDto.getCar().getType()),
                carWashes
        );

        sessions.put(userAccount.getId(),orderSession);

        // TODO send notification to all Car washes

        EndSessionTask endSessionTask = new EndSessionTask(sessions,userAccount.getId());
        timer.schedule(endSessionTask,120000);
    }

    @Override
    public void acceptOffer(String userPhoneNumber, int offerId)
            throws OrderSessionServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);
        OrderSession orderSession = getOrderSessionById(userAccount.getId());
        OrderOffer acceptedOffer = null;

        for (OrderOffer offer : orderSession.getOffers()) {
            if (offer.getId() == offerId) {
                acceptedOffer = offer;
                break;
            }
        }

        if (acceptedOffer == null){
            throw new OrderSessionServiceException("Odder with id " + offerId + " doesnt exists");
        }

        CarWash carWash = findCarWashByPhoneNumber(acceptedOffer.getCarWashPhoneNumber());

        List<WashService> washServices = washServiceRepository.findByCarWashAndCarTypeAndWashServiceTypeIn(
                carWash,
                orderSession.getCar().getCarType(),
                orderSession.getWashServiceTypes()
        );

        OrderInProcess orderInProcess = new OrderInProcess(
                orderSession.getCar(),
                acceptedOffer.getStartTime(),
                washServices
        );

        orderInProcessRepository.save(orderInProcess);
    }

    @Override
    public void createOffer(String carWashPhoneNumber, OfferCreationDto offerCreationDto, int orderId)
            throws OrderSessionServiceException{
        CarWash carWash = findCarWashByPhoneNumber(carWashPhoneNumber);
        OrderSession orderSession = getOrderSessionById(orderId);

        if(!orderSession.getCarWashes().contains(carWash)){
            throw new OrderSessionServiceException("Car wash with number " + carWashPhoneNumber +
                    " dont participate in order with id " + orderId);
        }

        OrderOffer offer = new OrderOffer(
                orderSession.getOffers().size(),
                offerCreationDto.getStartTime(),
                offerCreationDto.getFullPrice(),
                offerCreationDto.getWashTime(),
                carWash.getRating(),
                carWash.getAddress(),
                new MapPosition(
                        carWash.getLatitude(),
                        carWash.getLongitude()),
                carWash.getName(),
                carWashPhoneNumber);


        orderSession.getOffers().add(offer);
        if(orderSession.getBestRating() < carWash.getRating()){
            orderSession.setBestRating(carWash.getRating());
        }

        if(orderSession.getBestPrice() > offer.getPrice()){
            orderSession.setBestPrice(offer.getPrice());
        }
    }

    @Override
    public void declineOrder(String carWashPhoneNumber, int orderId)
            throws OrderSessionServiceException{
        CarWash carWash = findCarWashByPhoneNumber(carWashPhoneNumber);
        OrderSession orderSession = getOrderSessionById(orderId);
        orderSession.getCarWashes().remove(carWash);
    }

    @Override
    public List<OfferDto> getOffers(String userPhoneNumber)
            throws OrderSessionServiceException{
        UserAccount userAccount = findUserByPhoneNumber(userPhoneNumber);
        OrderSession orderSession = getOrderSessionById(userAccount.getId());
        return orderSession.getOffers().stream().map(this::orderOfferEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderInListDto> getOrders(String carWashPhoneNumber)
            throws OrderSessionServiceException{
        CarWash carWash = findCarWashByPhoneNumber(carWashPhoneNumber);

        List<OrderInListDto> orders = new ArrayList<>();

        for (OrderSession session : sessions.values()) {
            if (session.getCarWashes().contains(carWash)) {
                orders.add(orderSessionEntityToListDto(session));
            }
        }
        return orders;
    }

    @Override
    public OrderDto getOrder(String carWashPhoneNumber, int orderId) {
        CarWash carWash = findCarWashByPhoneNumber(carWashPhoneNumber);
        OrderSession orderSession = getOrderSessionById(orderId);
        List<WashService> washServices = washServiceRepository.findByCarWashAndCarTypeAndWashServiceTypeIn(
                carWash,
                orderSession.getCar().getCarType(),
                orderSession.getWashServiceTypes()
        );
        return orderSessionEntityToDto(orderSession,washServices);
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

    private CarWash findCarWashByPhoneNumber(String phoneNumber){
        Account account = findAccountByPhoneNumber(phoneNumber);
        CarWash carWash =  carWashRepository.findByAccount(account);
        if (carWash == null){
            throw new CarWashServiceException("Car wash with phone number "
                    + phoneNumber + "does not exists");
        }
        return carWash;
    }

    private List<CarWash> getCarWashesByOffer(OrderSessionCreationDto orderSessionCreationDto){
        List<CarWash> carWashes = carWashRepository.findCarWashesInSquare(
                orderSessionCreationDto.getSearchArea().getCenter().getLatitude(),
                orderSessionCreationDto.getSearchArea().getCenter().getLongitude(),
                orderSessionCreationDto.getSearchArea().getRadius());

        Iterator<CarWash> iterator = carWashes.iterator();
        while(iterator.hasNext()){
            CarWash carWash = iterator.next();
            if (!checkCarWashCoordinates(
                    carWash,
                    orderSessionCreationDto.getSearchArea().getCenter().getLatitude(),
                    orderSessionCreationDto.getSearchArea().getCenter().getLongitude(),
                    orderSessionCreationDto.getSearchArea().getRadius())
            ){
                iterator.remove();
                continue;
            }

            if (!checkCarWashServices(
                    carWash,
                    orderSessionCreationDto.getCar().getType(),
                    orderSessionCreationDto.getWashServiceTypes())
            ){
                iterator.remove();
            }
        }

        return carWashes;
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

    private boolean checkCarWashServices(
            CarWash carWash,
            CarType carType,
            List<WashServiceType> washServiceTypes
    ){
        List<WashService> washServices = washServiceRepository.findByCarWashAndCarTypeAndWashServiceTypeIn(
                carWash,
                carType,washServiceTypes
        );
        return washServiceTypes.size() == washServices.size();
    }

    private OrderInListDto orderSessionEntityToListDto(OrderSession orderSession){
        return new OrderInListDto(
                orderSession.getUserId(),
                orderSession.getBestPrice(),
                orderSession.getBestRating(),
                orderSession.getStartTime(),
                orderSession.getEndTime(),
                orderSession.getCar().getCarType(),
                orderSession.getCar().getNumber(),
                orderSession.getWashServiceTypes()
        );
    }

    private OrderDto orderSessionEntityToDto(OrderSession orderSession, List<WashService> washServices){
        return new OrderDto(
                orderSession.getUserId(),
                orderSession.getStartTime(),
                orderSession.getEndTime(),
                orderSession.getCar().getCarType(),
                orderSession.getCar().getNumber(),
                orderSession.getCar().getName(),
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

    private OfferDto orderOfferEntityToDto(OrderOffer offer){
        return new OfferDto(
                offer.getId(),
                offer.getStartTime(),
                offer.getPrice(),
                offer.getWashTime(),
                offer.getCarWashRating(),
                offer.getCarWashAddress(),
                offer.getCarWashLocation().getLatitude(),
                offer.getCarWashLocation().getLongitude(),
                offer.getCarWashName()
        );
    }

    private OrderSession getOrderSessionById(int orderId){
        OrderSession orderSession = sessions.get(orderId);
        if(orderSession == null){
            throw new OrderSessionServiceException("Order with id " + orderId + " does not exists");
        }
        return orderSession;
    }
}

@AllArgsConstructor
class EndSessionTask extends TimerTask{
    private Map<Integer,OrderSession> sessions;
    private int sessionId;
    @Override
    public void run(){
        sessions.remove(sessionId);
    }
}
