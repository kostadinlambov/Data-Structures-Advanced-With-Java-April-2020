package core;

import models.Car;

import java.util.*;
import java.util.stream.Collectors;

public class RaceManager implements Race {
    private final Map<Long, Car> carMapById;
    private final Set<Car> carsByDateAscAndIdAsc;

    private final Set<Car> carsByLapsTimeAsc;
    private final Set<Car> carsByHorsePowerDesc;
    private final Set<Car> carsByLapsCountDesc;

    private final Map<Date, Set<Car>> carMapByDate;

    public RaceManager() {
        this.carMapById = new HashMap<>();
        this.carsByDateAscAndIdAsc = new TreeSet<>(Comparator.comparing(Car::getJoinedOn).thenComparingLong(Car::getId));
        this.carsByLapsTimeAsc = new TreeSet<>(Comparator.comparingLong(Car::getBestLastTime));
        this.carsByHorsePowerDesc = new TreeSet<>(Comparator.comparingInt(Car::getHorsePower).reversed());
        this.carsByLapsCountDesc = new TreeSet<>(Comparator.comparingInt(Car::getLapsCount).reversed());

        this.carMapByDate = new HashMap<>();
    }

    @Override
    public void add(Car car) {
        boolean contains = this.contains(car);

        if (contains) {
            throw new UnsupportedOperationException();
        }

        this.carMapById.put(car.getId(), car);
        this.carsByDateAscAndIdAsc.add(car);

        this.carsByLapsTimeAsc.add(car);
        this.carsByHorsePowerDesc.add(car);
        this.carsByLapsCountDesc.add(car);

        this.carMapByDate.putIfAbsent(car.getJoinedOn(), new TreeSet<>(Comparator.comparing(Car::getJoinedOn)));
        this.carMapByDate.get(car.getJoinedOn()).add(car);
    }

    @Override
    public boolean contains(Car car) {
        return this.carMapById.containsKey(car.getId());
    }

    @Override
    public Car getCar(long id) {
        Car car = this.carMapById.get(id);

        if (car == null) {
            throw new UnsupportedOperationException();
        }

        return car;
    }

    @Override
    public Car removeCar(long id) {
        Car removedCar = this.carMapById.remove(id);

        if (removedCar == null) {
            throw new UnsupportedOperationException();
        }

        this.carsByDateAscAndIdAsc.remove(removedCar);

        this.carsByLapsTimeAsc.remove(removedCar);
        this.carsByHorsePowerDesc.remove(removedCar);
        this.carsByLapsCountDesc.remove(removedCar);

        this.carMapByDate.get(removedCar.getJoinedOn()).removeIf(car -> car.getId() == removedCar.getId());

        return removedCar;
    }

    @Override
    public int size() {
        return this.carMapById.size();
    }

    @Override
    public Collection<Car> getCarsByBestLapTime() {
        return this.carsByLapsTimeAsc;
    }

    @Override
    public Collection<Car> getCarsByHorsePower() {
        return this.carsByHorsePowerDesc;
    }

    @Override
    public Collection<Car> getCarsByLapsCount() {
        return this.carsByLapsCountDesc;
    }

    @Override
    public Collection<Car> getCarsJoinedAfter(Date date) {
        List<Car> result = new ArrayList<>();

        List<Date> dateAfterList = this.carMapByDate.keySet().stream()
                .filter(currentDate -> currentDate.after(date))
                .collect(Collectors.toList());

        for (Date currentDate : dateAfterList) {
            Set<Car> cars = this.carMapByDate.get(currentDate);
            result.addAll(cars);
        }

        return result;
    }

    @Override
    public Collection<Car> getCarsJoinedBefore(Date date) {
        List<Car> result = new ArrayList<>();

        List<Date> dateAfterList = this.carMapByDate.keySet().stream()
                .filter(currentDate -> currentDate.before(date))
                .collect(Collectors.toList());

        for (Date currentDate : dateAfterList) {
            Set<Car> cars = this.carMapByDate.get(currentDate);
            result.addAll(cars);
        }

        return result;
    }

    @Override
    public Collection<Car> getAllOrdered() {
        return this.carsByDateAscAndIdAsc;
    }
}
