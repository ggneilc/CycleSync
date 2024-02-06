package cyclesync.Bikes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cyclesync.Users.User;
import cyclesync.Rentals.Rental;
/**
 * @Author Neil Choromokos & Caleb Lemmons
 * Neil: Fixed associations
 * Caleb: Setup Class: Constructors, getters, setters
 * Holden: Added longitude & latitude
 */
@Entity
public class Bike {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;


    private String bikeName;
    private String type;
    private String rating;

    @ElementCollection
    private List<Integer> ratings;
    private boolean inUse;
    private boolean electric;
    private boolean onMarket;

    private String location;

    private int price;

    private Double latitude;
    private Double longitude;

    /*
     * @ManyToOne tells springboot that multiple instances of Phone can map to one instance of OR multiple rows of the phone table can map to one user row
     * @JoinColumn specifies the ownership of the key i.e. The Phone table will contain a foreign key from the User table and the column name will be user_id
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/phone objects (phone->user->[phones]->...)
     */
    //muliple bikes are owend by one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToOne
    @JsonIgnore
    private Rental rental;

     // =============================== Constructors ================================== //

    public Bike(){

    }

    public Bike(User user, String rating, String name, boolean electric, String location, int price, Double latitude, Double longitude, boolean onMarket) {
        this.user = user;
        this.rating = rating;
        this.bikeName = name;
        this.inUse = false;
        this.electric = electric;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.onMarket = true;
        this.ratings = new ArrayList<Integer>();
    }



    // =============================== Getters and Setters for each field ================================== //



    public boolean getonMarket(){return this.onMarket;}
    public void setOnMarket(boolean onMarket) {
        this.onMarket = onMarket;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getPrice(){return this.price;}

    public void setPrice(int price){this.price = price;}


    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        // Remove the bike from the previous user's list of bikes
        if (this.user != null) {
            this.user.getBikes().remove(this);
        }

        // Set the new user
        this.user = user;

        // Add the bike to the new user's list of bikes
        if (user != null && !user.getBikes().contains(this)) {
            user.getBikes().add(this);
        }
    }

    public boolean isElectric() {
        return electric;
    }
    public String getLocation(){return this.location;}

    public void setLocation(String location){
        this.location = location;
    }

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



    public void setElectric(boolean electric) {
        this.electric = electric;
    }


    public String getRating() {
        return this.rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getName() {
        return bikeName;
    }
    public void setName(String name) {
        this.bikeName = name;
    }


    public boolean getUse() {
        return inUse;
    }
    public void setUse(boolean val) {
        this.inUse = val;
    }

//    public void setRental(Rental rental) {
//        if (rental == null) {
//            if (this.rental != null) {
//                this.rental.setBike(null);
//            }
//        } else {
//            rental.setBike(this);
//        }
//        this.rental = rental;
//    }

    public void addRating(int newRating) {
        this.ratings.add(newRating);
        int total = 0;
        for (int i: this.ratings) {
            total += i;
        }
        this.rating = total / this.ratings.size() + "/5";
    }

    public Rental getRental() {return this.rental;}

}
