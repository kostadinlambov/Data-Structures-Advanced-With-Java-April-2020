package core;

import models.Car;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.Collectors;

public class RaceTest {

    private Race race;
    private List<Car> cars;

    @Before
    public void setUp() {
        this.race = new RaceManager();
        this.cars = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 19; i++) {
            Car car = new Car(i + 1, random.nextInt(1000), random.nextInt(500), 50 - i, new Date());
            this.cars.add(car);
            this.race.add(car);
        }
    }

    @Test
    public void add() {
        assertTrue(this.race.contains(this.cars.get(0)));
        Car car = new Car(240, 250, 260, 2, new Date());
        this.race.add(car);
        assertTrue(this.race.contains(car));
    }

    @Test
    public void contains() {
        for (Car car : cars) {
            assertTrue(this.race.contains(car));
        }
    }

    @Test
    public void get() {
        for (Car car : cars) {
            Car c = this.race.getCar(car.getId());
            assertNotNull(c);
            assertEquals(car.getId(), c.getId());
        }
    }

    @Test
    public void remove() {
        for (Car car : cars) {
            Car c = this.race.removeCar(car.getId());
            assertNotNull(c);
            assertEquals(car.getId(), c.getId());
        }
    }

    @Test
    public void size() {
        assertEquals(this.cars.size(), this.race.size());
        assertEquals(0, new RaceManager().size());
    }

    @Test
    public void getCarsByBestLapTime() {
        Collection<Car> carsByBestTime = this.race.getCarsByBestLapTime();

        assertNotNull(carsByBestTime);
        List<Car> expected = carsByBestTime.stream()
                .sorted(Comparator.comparing(Car::getBestLastTime))
                .collect(Collectors.toList());

        assertEquals(expected.size(), carsByBestTime.size());

        List<Car> actual = new ArrayList<>(carsByBestTime);

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}