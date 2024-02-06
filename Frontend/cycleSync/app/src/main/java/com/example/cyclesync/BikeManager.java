package com.example.cyclesync;

public class BikeManager {
    private static BikeManager instance;
    private Bike bike;

    public static BikeManager getInstance() {
        if(instance == null) {
            instance = new BikeManager();
        }
        return instance;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Bike getBike() {
        return this.bike;
    }

    public void clearBike(){
        this.bike = null;
    }
}
