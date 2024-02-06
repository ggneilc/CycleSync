package com.example.cyclesync;

public class Bike {
    private int id;
    private String name;
    private String rating;
    private boolean inUse;
    private boolean electric;
    private String location;
    private int price;
    private Double latitude;
    private boolean onMarket;
    private Double longitude;
    private Rental rental;

    public Bike(int id, String bikeName, String rating, boolean inUse, boolean electric, String location, int price, Double latitude, Double longitude, boolean onMarket) {
        this.id = id;
        this.name = bikeName;
        this.rating = rating;
        this.inUse = inUse;
        this.electric = electric;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.onMarket = onMarket;
    }

    // =============================== Getters and Setters for each field ================================== //



    public boolean getonMarket(){return this.onMarket;}
    public void setonMarket(boolean onMarket){this.onMarket = onMarket;}
    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public Double getLatitude( ){
        return this.latitude = latitude;
    }
    public Double getLongitude( ){
        return this.longitude = longitude;
    }

    public boolean isElectric() {
        return electric;
    }

    public void setElectric(boolean electric) {
        this.electric = electric;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUse() {
        return inUse;
    }

    public void setUse(boolean use) {
        this.inUse = use;
    }

    public String getLocation(){return this.location;}

    public void setLocation(String location){this.location=location;}

    public String getRating() {return this.rating;}

    public boolean isInUse(){return this.inUse;}

    public Rental getRental() {return this.rental;}

    @Override
    public String toString() {
        return "Bike{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", inUse=" + inUse +
                ", electric=" + electric +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", latitude=" + latitude +
                ", onMarket=" + onMarket +
                ", longitude=" + longitude +
                ", rental=" + rental +
                '}';
    }


}
