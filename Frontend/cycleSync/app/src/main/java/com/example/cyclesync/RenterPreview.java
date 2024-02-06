package com.example.cyclesync;

import java.util.Date;


public class RenterPreview {
    private String renter_name;
    private String bike_name;
    private int renter_id;

    // =============================== Constructors ================================== //

    public RenterPreview(String renter_name, String bike_name, int renter_id) {
        this.renter_name = renter_name;
        this.bike_name = bike_name;
        this.renter_id = renter_id;
    }


    // =============================== Getters and Setters for each field ================================== //

    public String getRenter_name() {
        return renter_name;
    }

    public void setRenter_name(String renter_name) {
        this.renter_name = renter_name;
    }

    public String getBike_name() {
        return bike_name;
    }

    public void setBike_name(String bike_name) {
        this.bike_name = bike_name;
    }

    public int getRenter_id() {
        return renter_id;
    }

    public void setRenter_id(int renter_id) {
        this.renter_id = renter_id;
    }
}
