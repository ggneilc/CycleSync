package cyclesync.Users;

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

import cyclesync.Bikes.Bike;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String emailId;
    private Date joiningDate;
    private boolean isRenter;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bike_id")
    private Bike bike;*/

    @OneToMany
    private List<Bike> bikes;

     // =============================== Constructors ================================== //
    public User(String name, String emailId, Date joiningDate) {
        this.name = name;
        this.emailId = emailId;
        this.joiningDate = joiningDate;
        this.isRenter = false;
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

    public Date getJoiningDate(){
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate){
        this.joiningDate = joiningDate;
    }

    public boolean getIsRenter(){
        return isRenter;
    }

    public void setIsRenter(boolean isRenter){
        this.isRenter = isRenter;
    }

    //public void setBike(Bike bike){                                 ////Probably more here
        //this.bike = bike;
    //}

    public List<Bike> getBikes() {
        return bikes;
    }

    public void addBike(Bike bike){
        this.bikes.add(bike);
    }
}
