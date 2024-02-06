package onetomany.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import onetomany.Bikes.Bike;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private String password;
    private String emailId;
    private Date joiningDate;
    private boolean ifActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bike_id")
    private Bike bike;

    @OneToMany
    private List<Bike> bikes;

     // =============================== Constructors ================================== //
    public User(String name, String emailId, String password, Date joiningDate) {
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.joiningDate = joiningDate;
        this.ifActive = true;
        bikes = new ArrayList<>();
    }

    public User() {
        bikes = new ArrayList<>();
    }

    // =============================== Getters and Setters for each field ================================== //
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }

    public String getPass(){
        return password;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public Date getJoiningDate(){
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate){
        this.joiningDate = joiningDate;
    }

    public boolean getIsActive(){
        return ifActive;
    }

    public void setIfActive(boolean ifActive){
        this.ifActive = ifActive;
    }

    public void setBike(Bike bike){                                 ////Probably more here
        this.bike = bike;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void addBike(Bike bike){
        this.bikes.add(bike);
    }
}
