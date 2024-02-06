package com.example.cyclesync;

import java.util.Date;

public class RentalPreview {
    private String renter_name;
    private String rating;

    private String bike_name;

    private boolean electric;
    private int bike_id;

    private Date start_date;
    private Date stop_date;
    private int price;
    private String location;
    private String review;

    // =============================== Constructors ================================== //

    public RentalPreview(String renter_name, String rating, String bike_name, boolean electric, int bike_id, Date start_date, Date stop_date, int price, String location, String review) {
        this.renter_name = renter_name;
        this.rating = rating;
        this.bike_name = bike_name;
        this.electric = electric;
        this.bike_id = bike_id;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.price = price;
        this.location = location;
        this.review = review;
    }


    // =============================== Getters and Setters for each field ================================== //


    public String getRenter_name() {
        return renter_name;
    }

    public void setRenter_name(String renter_name) {
        this.renter_name = renter_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getBike_id() {
        return bike_id;
    }

    public void setBike_id(int bike_id) {
        this.bike_id = bike_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getStop_date() {
        return stop_date;
    }

    public void setStop_date(Date stop_date) {
        this.stop_date = stop_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBike_name() { return bike_name; }

    public boolean isElectric() {return electric;}

    public String getReview() { return review; }

    @Override
    public String toString() {
        return "RentalPreview{" +
                "renter_name='" + renter_name + '\'' +
                "bike_name='" + bike_name + '\'' +
                "electric='" + electric + '\'' +
                ", rating='" + rating + '\'' +
                ", bike_id=" + bike_id +
                ", start_date=" + start_date +
                ", stop_date=" + stop_date +
                ", price=" + price +
                ", location='" + location + '\'' +
                '}';
    }
}
