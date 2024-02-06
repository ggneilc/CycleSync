package cyclesync.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cyclesync.Rentals.Rental;
import cyclesync.Rentals.RentalRepository;
import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Users.User;
import cyclesync.Users.UserRepository;


/**
 * 
 * @author Neil Choromokos & Caleb Lemmons
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RentalRepository rentalRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    
    // -------------------- Basic Crud Operations --------------------- //
    // ----- Getting User by All/Id/email ----- //
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }
    @GetMapping(path = "/users/email/{email}")
    User getUserByEmail( @PathVariable String email){
        return userRepository.findByEmailId(email);
    }

    // ----- Login User ----- //
    @PostMapping(path = "/users/login")
    User checkUser(@RequestBody User request){
        User user = userRepository.findByEmailId(request.getEmailId());
        if (user.getPassword().equals(request.getPassword()))
            return user;
        else
            System.out.print(failure);
        return null;
    }

    // ----- Create User ----- //
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    // ----- Update User by ID ----- //
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }   
    
    // ----- Delete User by ID ----- //
    @DeleteMapping(path = "/users/{id}")
    String deleteRental(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
    //----------------------------------------------------------------//


    //set User's current rental to {rentalId}
    @PutMapping("/users/{userId}/rentals/{rentalId}")
    String assignRentalToUser(@PathVariable int userId,@PathVariable int rentalId){
        User user = userRepository.findById(userId);
        Rental rental = rentalRepository.findById(rentalId);
        if(user == null || rental == null)
            return failure;
        rental.setRider(user);
        user.setRental(rental);
        userRepository.save(user);
        return success;
    }

    //add to favorites
    @PutMapping("/users/{userId}/rentals/{rentalId}/favorite")
    String addRentalToUser(@PathVariable int userId,@PathVariable int rentalId){
        User user = userRepository.findById(userId);
        Rental rental = rentalRepository.findById(rentalId);
        if(user == null || rental == null)
            return failure;
        user.addFavorite(rental);
        userRepository.save(user);
        return success;
    }
}
