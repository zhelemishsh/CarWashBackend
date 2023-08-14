package com.pomika.carwashbackend.repository;

import com.pomika.carwashbackend.model.Account;
import com.pomika.carwashbackend.model.CarWash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarWashRepository extends JpaRepository<CarWash, Integer> {
    CarWash findByAccount(Account account);

    List<CarWash> findByMapPosition_LatitudeIsBetweenAndMapPosition_LongitudeIsBetween(double latitudeBottom,
                                                               double latitudeTop,
                                                               double longitudeBottom,
                                                               double longitudeTop);

    default List<CarWash> findCarWashesInSquare(double centerLatitude, double centerLongitude, double radius){
        double latitudeShift = (180 * radius)/(Math.PI * 6378100);
        double longitudeShift = (180 * radius)/(Math.PI * 6356800);
        return findByMapPosition_LatitudeIsBetweenAndMapPosition_LongitudeIsBetween(
                centerLatitude - latitudeShift,
                centerLatitude + latitudeShift,
                centerLongitude - longitudeShift,
                centerLongitude + longitudeShift
        );
    }
}
