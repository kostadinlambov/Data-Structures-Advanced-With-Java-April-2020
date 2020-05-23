package models;

import java.util.Date;
import java.util.Objects;

public class Car {
    private long id;
    private long bestLastTime;
    private int horsePower;
    private int lapsCount;
    private Date  joinedOn;

    public Car(long id, long bestLastTime, int horsePower, int lapsCount, Date joinedOn) {
        this.id = id;
        this.bestLastTime = bestLastTime;
        this.horsePower = horsePower;
        this.lapsCount = lapsCount;
        this.joinedOn = joinedOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBestLastTime() {
        return bestLastTime;
    }

    public void setBestLastTime(long bestLastTime) {
        this.bestLastTime = bestLastTime;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public int getLapsCount() {
        return lapsCount;
    }

    public void setLapsCount(int lapsCount) {
        this.lapsCount = lapsCount;
    }

    public Date getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(Date joinedOn) {
        this.joinedOn = joinedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
