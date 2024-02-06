package cyclesync.Rentals;

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
 * @Author Neil Choromokos & Caleb Lemmons
 */

@RestController
public class RentalController {

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BikeRepository bikeRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //-------------------------- CRUD Operations ------------------// 
    //Renters can create/update/list/delete their rentals
    @GetMapping(path = "/rentals")
    List<Rental> getAllRentals(){
        return rentalRepository.findAll();
    }
    @GetMapping(path = "/rentals/{id}")
    Rental getRentalById(@PathVariable int id){
        return rentalRepository.findById(id);
    }

    //User who created rental is set to RenterID, therefore need user
    @PostMapping(path = "/users/{id}/rentals/bike/{bikeid}")
    String createRental(@RequestBody Rental rental, @PathVariable int id, @PathVariable int bid){
        User user = userRepository.findById(id);
        Bike bike = bikeRepository.findById(bid);
        if (rental == null)
            return failure;
        rental.setRenter(user);
        rental.setBike(bike);
        rentalRepository.save(rental);
        return success;
    }

    //Update Rental Information -> Renter editing information
    @PutMapping(path = "/rentals/{id}")
    Rental updateRental(@PathVariable int id, @RequestBody Rental request){
        Rental rental = rentalRepository.findById(id);
        if(rental == null)
            return null;
        rentalRepository.save(request);
        return rentalRepository.findById(id);
    }


    @DeleteMapping(path = "/rentals/{id}")
    String deleteRental(@PathVariable int id){
        rentalRepository.deleteById(id);
        return success;
    }
}
