package core;

import models.Car;

import java.util.Collection;
import java.util.Date;

public interface Race {
    void add(Car car);

    boolean contains(Car car);

    Car getCar(long id);

    Car removeCar(long id);

    int size();

    Collection<Car> getCarsByBestLapTime();

    Collection<Car> getCarsByHorsePower();

    Collection<Car> getCarsByLapsCount();

    Collection<Car> getCarsJoinedAfter(Date date);

    Collection<Car> getCarsJoinedBefore(Date date);

    Collection<Car> getAllOrdered();
}
