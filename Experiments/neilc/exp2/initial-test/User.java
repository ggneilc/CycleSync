package cyclesync.Users;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import cyclesync.Bikes.Bike;
import cyclesync.Rentals.Rental;

@Entity
@Table(name="USERS")
public class User {

    //set id to primary key, generate from 1-> infty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isRenter;

    //one user has many bikes -> map by 'user' field in Bike.java
    @OneToMany(mappedBy="user")
    @JsonIgnore
    private List<bike> bikes;

    @OneToMany(mappedBy="user")
    private List<rental> rentals;

    // -------------Constructor--------------- //

    public User(String fname, String lname, String email, String pass){
        this.firstName = fname;
        this.lastName = lname;
        this.email = email;
        this.password = pass;
        this.isRenter = false;
        bikes = new ArrayList<>();
        rentals = new ArrayList<>();
    }
    public User() {
        bikes = new ArrayList<>();
        rentals = new ArrayList<>();
    }
    public boolean isRenter() {
        return isRenter;
    }

    // -------------Add/Delete Bike/Rental----------- //

    public void addBike(Bike bike){
        this.bikes.add(bike);
    }
    public void addRental(Rental rental){
        this.rentals.add(rental);
    }

    public void delBike(Bike bike){
        this.bikes.remove(bike);
    }
    public void delRental(Rental rental){
        this.rentals.remove(rental);
    }


    // --------------Getters & Setters------------ //
    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPassword() {
        return password;
    }
    public List<bike> getBikes() {
        return bikes;
    }
    public List<rental> getRentals() {
        return rentals;
    }
    // ------------------------------------------------ //
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRenter(boolean renter) {
        isRenter = renter;
    }
    public void setBikes(List<bike> bikes) {
        this.bikes = bikes;
    }
    public void setRentals(List<rental> rentals) {
        this.rentals = rentals;
    }
}

