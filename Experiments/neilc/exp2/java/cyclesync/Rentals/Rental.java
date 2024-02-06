package cyclesync.Rentals;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import cyclesync.Users.user;
import cyclesync.Bikes.bike;

public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumns(name="rentalID", nullable=false)
    private User user;

    private int price;
    private String date;
    private int status; //0 for completed, 1 for in progres


    // -------------- Constructors --------------- //
    public Rental(int price, String date, int status) {
        this.price = price;
        this.date = date;
        this.status = status;
    }
    public Rental() {}

    // ----------- Getters & Setters ------------//


    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

