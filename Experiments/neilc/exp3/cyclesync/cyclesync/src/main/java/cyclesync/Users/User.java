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

import cyclesync.Rentals.Rental;
import cyclesync.Bikes.Bike;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 */


@Entity
public class User {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String first_name;
    private String last_name;
    private String emailId;
    private String password;
    private Date joiningDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_rental_id")
    private Rental current_rental;
    @OneToMany
    private List<Rental> favorited_rentals;
    private boolean isRenter;
    @OneToMany
    private List<Bike> bikes;
    @OneToMany
    private List<Rental> rentals;

     // =============================== Constructors ================================== //


    public User(String first, String last, String emailId, String password) {
        this.first_name = first;
        this.last_name = last;
        this.emailId = emailId;
        this.password = password;
        this.joiningDate = new Date();

        this.current_rental = null;
        favorited_rentals = new ArrayList<>();

        this.isRenter = false;
        bikes = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public User() {
        favorited_rentals = new ArrayList<>();
        bikes = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    
    // =============================== Getters and Setters for each field ================================== //


    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }


    public String getFirstName(){
        return first_name;
    }
    public void setFirstName(String name){
        this.first_name = name;
    }


    public String getLastName(){
        return last_name;
    }
    public void setLastName(String name){
        this.last_name = name;
    }


    public void setEmailId(String emailId){
        this.emailId = emailId;
    }
    public String getEmailId(){
        return emailId;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public Date getJoiningDate(){
        return joiningDate;
    }


    public void setRental(Rental rental){
        this.current_rental = rental;
    }
    public Rental getRental(){
        return current_rental;
    }


    public void setFavorites(List<Rental> rentals) {
        this.favorited_rentals = rentals;
    }
    public List<Rental> getFavorites() {
        return favorited_rentals;
    }
    public void addFavorite(Rental rental){
        this.favorited_rentals.add(rental);
    }




    public void setIsRenter(boolean ifActive){
        this.isRenter = ifActive;
    }
    public boolean getIsRenter(){
        return isRenter;
    }


    

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }
    public List<Bike> getBikes() {
        return bikes;
    }
    public void addBikes(Bike bike){
        this.bikes.add(bike);
    }


    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
    public List<Rental> getRentals() {
        return rentals;
    }
    public void addRental(Rental rental){
        this.rentals.add(rental);
    }
    
}
