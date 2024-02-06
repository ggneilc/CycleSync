package com.example.cyclesync;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class User {

    private int id;

    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private Date joiningDate;

    private Rental current_rental;
    private List<Rental> favorited_rentals;

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    private List<PaymentMethod> paymentMethods; // The user's payment methods
    private boolean isRenter;
    private List<Bike> bikes;
    private List<Rental> rentals;

    public Rental getCurrent_rental() {
        return current_rental;
    }

    public void setCurrent_rental(Rental current_rental) {
        this.current_rental = current_rental;
    }

    // Constructors
    public User(String first, String last, String emailId, String password) {
        this.firstName = first;
        this.lastName = last;
        this.emailId = emailId;
        this.password = password;
        this.joiningDate = new Date();
        this.current_rental = null;
        this.favorited_rentals = new ArrayList<>();
        this.isRenter = false;
        this.bikes = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }
    public User() {
        this.favorited_rentals = new ArrayList<>();
        this.bikes = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }



    // =============================== Getters and Setters for each field ================================== //


    public String getName(){
        return firstName +" "+ lastName;
    }

    public void setName(String name){
        this.firstName = name;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }

    public Date getJoiningDate(){
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate){
        this.joiningDate = joiningDate;
    }

    public boolean getIsRenter(){
        return isRenter;
    }

    public int getId() {return id;}

    public void setIsRenter(boolean ifActive){
        this.isRenter = ifActive;
    }


    public boolean isRenter() {
        return isRenter;
    }

    public void setBikes(List<Bike> bikes) { this.bikes = bikes; }

    public List<Bike> getBikes() { return bikes; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                ", joiningDate=" + joiningDate +
                ", current_rental=" + (current_rental != null ? current_rental.toString() : "null") +
                ", favorited_rentals=" + favorited_rentals +
                ", isRenter=" + isRenter +
                ", bikes=" + bikes +
                ", rentals=" + rentals +
                '}';
    }

}

