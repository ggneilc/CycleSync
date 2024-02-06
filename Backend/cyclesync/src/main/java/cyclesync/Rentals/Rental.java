package cyclesync.Rentals;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cyclesync.Users.User;
import cyclesync.Bikes.Bike;
/**
 * @Author Neil Choromokos & Caleb Lemmons
 * Neil: Set up Class: Constructors, getters, setters
 * Thomas: added variables & methods for the variables
 */

@Entity
public class Rental {
    
    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    private int renter_id;
    private int rider_id;
    private int bike_id;

    private Date date;
    private Date stop_date;

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    private int paymentMethodId;
    private String status; //can be "Open", "inUse"
    private int rating;
    private String review;
    private int price;
    
     // =============================== Constructors ================================== //


    public Rental(Bike bike, User user, int paymentMethodId) {
        this.renter_id = bike.getUser().getId();
        this.rider_id = user.getId();
        this.bike_id = bike.getId();
        this.date = new Date();
        this.status = "active";
        this.price = bike.getPrice();
        this.paymentMethodId = paymentMethodId;
    }

    public Rental() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }


    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public int getPrice(){
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }

    public Date getDate(){
        return date;
    }

    public int getRenter(){
        return renter_id;
    }
    public int getRenter_id() {return renter_id;}
    public void setRenter(User user){
        this.renter_id = user.getId();
    }

    public int getRider(){
        return rider_id;
    }
    public void setRider(User user){
        this.rider_id = user.getId();
    }

    public int getBikeId() { return bike_id; }
    public void setStopDate(Date date) {
        this.stop_date = date;
    }
    public Date getStop_date() {
        return this.stop_date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


//    public void setBike(Bike bike) {
//        // Retroactively changing the bike on a rental is very unlikely to happen
//        // If there's an existing association between the rental and a bike, remove it
//        if (this.bike_id != null) {
//            this.bike.setRental(null);
//        }
//
//        // Set the new bike for this rental
//        this.bike_id = bike.getId();
//
//        // If the passed bike is not null and its associated rental isn't this instance, fix the association
//        if (bike != null && bike.getRental() != this) {
//            bike.setRental(this);
//        }
//    }

}
