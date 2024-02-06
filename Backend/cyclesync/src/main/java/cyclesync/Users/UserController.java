package cyclesync.Users;

import java.sql.Array;
import java.util.*;

import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Rentals.RentalPreview;
import cyclesync.Rentals.RenterPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cyclesync.Rentals.Rental;
import cyclesync.Rentals.RentalRepository;
import cyclesync.Websockets.RiderRenterMessaging;


/**
 * 
 * @author Neil Choromokos & Caleb Lemmons
 * Neil: Setup class: Constructor, getters, setters
 * Thomas: Added rental preview
 * Holden: Added bike inventory
 */ 

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    BikeRepository bikeRepository;


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
        return userRepository.findById(id).orElse(null);
    }
    @GetMapping(path = "/users/email/{email}")
    User getUserByEmail( @PathVariable String email){
        return userRepository.findByEmailId(email);
    }

    // ----- Login User ----- //
    @PostMapping(path = "/users/login")
    User checkUser(@RequestBody User request){
        logger.info("Received login request for user: " + request.getEmailId() + " with password: " + request.getPassword());
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
        User user = userRepository.findById(id).orElse(null);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id).orElse(null);
    }   
    
    // ----- Delete User by ID ----- //
    @DeleteMapping(path = "/users/{id}")
    String deleteRental(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
    //----------------------------------------------------------------//


    @PutMapping("/messaging/{senderID}/{receiverID}")
    String saveMessage(@PathVariable int senderID, @PathVariable int receiverID){
        User sender = userRepository.findById(senderID).orElse(null);
        User receiver = userRepository.findById(receiverID).orElse(null);



        return success;
    }

    @PutMapping("/users/{userId}/rentals/{rentalId}")
    String assignRentalToUser(@PathVariable int userId, @PathVariable int rentalId){
        User user = userRepository.findById(userId).orElse(null);
        Rental rental = rentalRepository.findById(rentalId).orElse(null);

        if(user == null || rental == null) {
            return failure;
        }

        rental.setRider(user);           // Sets the rider in the Rental object
        user.setCurrentRental(rental);   // Sets the current rental in the User object
        userRepository.save(user);       // Saves the updated User object

        return success;
    }



    //add to favorites
    @PutMapping("/users/{userId}/rentals/{rentalId}/favorite")
    String addRentalToUser(@PathVariable int userId,@PathVariable int rentalId){
        User user = userRepository.findById(userId).orElse(null);
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if(user == null || rental == null)
            return failure;
        user.addFavorite(rental);
        userRepository.save(user);
        return success;
    }

    @GetMapping("/users/{userId}/currentRental")
    Rental getCurrentRental(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getRental();
    }

    @GetMapping("/users/{userId}/getRenters")
    List<RenterPreview> getRenters(@PathVariable int userId) {
        List<Rental> rentals = rentalRepository.findAll();
        List<RenterPreview> renterPreviews = new ArrayList<>();
        for (Rental r: rentals) {
            if (r.getStatus().equals("active") && r.getRenter_id() == userId && r.getRider() != userId) {
                Optional<User> userOptional = userRepository.findById(r.getRider());
                if (userOptional.isEmpty()) {
                    return null;
                }
                Optional<Bike> bikeOptional = bikeRepository.findById(r.getBikeId());
                if (bikeOptional.isEmpty()) {
                    return null;
                }
                User user = userOptional.get();
                Bike bike = bikeOptional.get();
                renterPreviews.add(new RenterPreview(user.getFirstName() + " " + user.getLastName(), bike.getName(), user.getId()));
            }
        }
        return renterPreviews;
    }
}
