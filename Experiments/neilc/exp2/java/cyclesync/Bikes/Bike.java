package cyclesync.Bikes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import cyclesync.Users.user;

@Entity
@Table(name="BIKES")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //one user has many bikes -> many bikes have one user
    @ManyToOne
    @JoinColumn(name="bikeId",nullable=false)
    private User user;

    private String bikeName;
    private String type;
    private String color;
    private int price;

    // ----------------- Constructor ----------------- //
    public Bike(String name, String type, String color, int price){
        this.bikeName = name;
        this.type = type;
        this.color = color;
        this.price = price;
    }
    public Bike() {}
    public void setUser(User user){
        this.user = user;
    }

    // ------------------ Getters & Setters -------------- //

    public int getId() {
        return id;
    }
    public int getPrice() {
        return price;
    }
    public String getBikeName() {
        return bikeName;
    }
    public String getColor() {
        return color;
    }
    public String getType() {
        return type;
    }
    public User getUser() {
        return user;
    }
    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setId(int id) {
        this.id = id;
    }
}