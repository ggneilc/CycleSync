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
    @OneToOne
    @JsonIgnore
    private User renter_id;
    @OneToOne
    @JsonIgnore
    private User rider_id;
    @OneToOne
    @JsonIgnore
    private Bike bike;

    private Date date;
    private String status; //can be "Open", "inUse"
    private int price;
    
     // =============================== Constructors ================================== //


    public Rental(int price) {
        this.status = "Open";
        this.price = price;
        this.date = new Date();
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

    public User getRenter(){
        return renter_id;
    }
    public void setRenter(User user){
        this.renter_id = user;
    }

    public User getRider(){
        return rider_id;
    }
    public void setRider(User user){
        this.rider_id = user;
    }

    public Bike getBike() { return bike; }
    public void setBike(Bike bike) {this.bike = bike;}
}
