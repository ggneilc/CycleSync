package com.example.cyclesync;

import java.util.Date;

public class Rental {

    private int id;
    private int renter_id;
    private int rider_id;
    private int bike;

    private Date date;
    private String status;

    private int paymentMethodId;
    private int price;

    // Constructors
    public Rental(int bike) {
        this.bike = bike;
    }

    public Rental() {
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public int getRenterId() {
        return renter_id;
    }

    public void setRenter_id(int id) {this.renter_id = id;}

    public void setRental(int user) {
        this.renter_id = user;
    }

    public int getRider() {
        return rider_id;
    }

    public void setRider(int user) {
        this.rider_id = user;
    }

    public int getBike() {
        return bike;
    }
}
