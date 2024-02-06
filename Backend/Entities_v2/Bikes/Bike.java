package onetomany.Bikes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetomany.Users.User;

@Entity
public class Bike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String manufacturer;
    private String color;
    private String condition;
    private boolean inUse;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

     // =============================== Constructors ================================== //
    public Bike( String manufacturer, String color, String condition, String name, boolean inUse) {
        this.manufacturer = manufacturer;
        this.color = color;
        this.condition = condition;
        this.name = name;
        this.inUse = false;
    }

    public Bike( ) {
     Bike bike = null;
    }

    // =============================== Getters and Setters for each field ================================== //
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsage()
    {
        if(inUse) {return "Bike is currently being rented.";}

        else {return "Bike is ready to be rented!";}
    }

    public void setUsage(boolean inUse) {
        this.inUse = inUse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
