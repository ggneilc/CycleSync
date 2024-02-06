package cyclesync.Rentals;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cyclesync.Users.User;
import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Users.UserRepository;
import cyclesync.Websockets.GlobalBikeMap;


/**
 * @Author Neil Choromokos & Caleb Lemmons
 * Neil: Setup class: Constructors, getters, setters
 * Thomas: Added rental Previews
 */

@RestController
public class RentalController {

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BikeRepository bikeRepository;
    @Autowired
    private GlobalBikeMap globalBikeMap;

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
        return rentalRepository.findById(id).orElse(null);
    }

    @GetMapping(path="/rentals/user/{userId}")
    List<RentalPreview> getRentalsByUserId(@PathVariable int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return Collections.emptyList();
        }
        User user = optionalUser.get();
        List<Rental> rentals = user.getRentals();
        List<RentalPreview> results = new ArrayList<RentalPreview>();
        for (Rental rental: rentals) {
            Optional<User> optionalRenter = userRepository.findById(rental.getRenter_id());
            if (!optionalRenter.isPresent()) {
                return results;
            }
            User renter = optionalRenter.get();
            String renter_name = renter.getFirstName() + " " + renter.getLastName();
            Optional<Bike> optionalBike = bikeRepository.findById((rental.getBikeId()));
            if (!optionalBike.isPresent()) {
                return results;
            }
            Bike bike = optionalBike.get();
            String rating = rental.getRating() + "/5";
            RentalPreview rentalPreview = new RentalPreview(renter_name, rating, bike.getName(), bike.isElectric(), bike.getId(), rental.getDate(), rental.getStop_date(), bike.getPrice(), bike.getLocation(), rental.getReview());
            results.add(rentalPreview);
        }
        return results;
    }

    //User who created rental is set to RenterID, therefore need user
//    @PostMapping(path = "/users/{id}/rentals/bike/{bikeid}")
//    String createRental(@RequestBody Rental rental, @PathVariable int id, @PathVariable int bid){
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (!optionalUser.isPresent()) {
//            return failure;
//        }
//        User user = optionalUser.get();
//
//        Optional<Bike> optionalBike = bikeRepository.findById(bid);
//        if (!optionalBike.isPresent()) {
//            return failure;
//        }
//        Bike bike = optionalBike.get();
//
//        if (rental == null) {
//            return failure;
//        }
//
//        rental.setRenter(user);
//        rental.setBike(bike);
//        rentalRepository.save(rental);
//
//        String rentalJson = "{\"alertType\":\"bikeOpen\", \"bikeId\":"+bid+"}";
//        globalBikeMap.broadcast(rentalJson);
//
//        return success;
//    }



    // Initiate rental for a user
    @GetMapping(path="/users/{id}/rent/{bikeId}/{paymentMethodId}")
    String initiateRental(@PathVariable int id, @PathVariable int bikeId, @PathVariable int paymentMethodId) {
        // Check if user exists
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return failure;
        }
        User user = optionalUser.get();
        // Check validity to rent
        if (user.getRental() != null) {
            // Already renting something!
            return failure;
        }

        Optional<Bike> optionalBike = bikeRepository.findById(bikeId);
        if (!optionalBike.isPresent()) {
            return failure;
        }

        Bike bike = optionalBike.get();

        if (!bike.getonMarket()) {
            return failure;
        }

        // Create new rental
        Rental newRental = new Rental(bike, user, paymentMethodId);
        rentalRepository.save(newRental);

        // Set current rental
        user.setCurrentRental(newRental);

        // Add to crental history
        user.addRental(newRental);
        userRepository.save(user);

        // Mark bike as off market
        bike.setOnMarket(false);
        bikeRepository.save(bike);

        String rentalJson = "{\"alertType\":\"bikeRented\", \"bikeId\":"+bikeId+"}";
        globalBikeMap.broadcast(rentalJson);

        return success;
    }

    @GetMapping(path="/rentals/stop/{userId}")
    String stopRental(@PathVariable int userId) {
        // Remove rental from user
        Optional<User> optionalRenter = userRepository.findById(userId);
        if (!optionalRenter.isPresent()) {
            return failure;
        }

        User renter = optionalRenter.get();

        // Check if rental exists
        Rental rental = renter.getRental();
        if (rental == null) {
            return failure;
        }

        // Check if rental is active
        if (!rental.getStatus().equals("active")) {
            // rental is not active
            return failure;
        }

        renter.setCurrentRental(null);

        Optional<Bike> optionalBike = bikeRepository.findById(rental.getBikeId());
        if (!optionalBike.isPresent()) {
            return failure;
        }

        Bike bike = optionalBike.get();
        bike.setOnMarket(true);

        rental.setStatus("inactive");
        rental.setStopDate(new Date());

        userRepository.save(renter);
        bikeRepository.save(bike);

        String rentalJson = "{\"alertType\":\"bikeOpen\", \"bikeId\":"+bike.getId()+"}";
        globalBikeMap.broadcast(rentalJson);

        return success;
    }

    //Update Rental Information -> Renter editing information
    @PutMapping(path = "/rentals/{id}")
    Rental updateRental(@PathVariable int id, @RequestBody Rental request){
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (!optionalRental.isPresent()) {
            // Handle not found case
            return null; // or some appropriate error response
        }
        Rental rental = optionalRental.get();

        if(rental == null)
            return null;
        rentalRepository.save(request);
        return rentalRepository.findById(id).orElse(null);
    }


    @DeleteMapping(path = "/rentals/{id}")
    String deleteRental(@PathVariable int id){
        rentalRepository.deleteById(id);
        return success;
    }

    @GetMapping(path="/rentals/wipe")
    String wipeRentals() {
        rentalRepository.deleteAll();
        return success;
    }

    @PostMapping(path="/rentals/review/{id}")
    String reviewRental(@PathVariable int id, @RequestBody RentalReviewRequest request) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (!optionalRental.isPresent()) {
            // Handle not found case
            return failure; // or some appropriate error response
        }
        Rental rental = optionalRental.get();

        rental.setRating(request.getRating());
        rental.setReview(request.getReview());

        Optional<Bike> optionalBike = bikeRepository.findById(rental.getBikeId());
        if (!optionalBike.isPresent()) {
            return failure;
        }
        Bike bike = optionalBike.get();

        bike.addRating(request.getRating());

        bikeRepository.save(bike);

        rentalRepository.save(rental); // Save changes to the DB

        return success;
    }
}
