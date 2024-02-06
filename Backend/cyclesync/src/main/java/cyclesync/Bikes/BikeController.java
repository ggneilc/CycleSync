package cyclesync.Bikes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import cyclesync.Users.User;
import cyclesync.Users.UserRepository;
import java.util.Optional;
import cyclesync.Websockets.GlobalBikeMap;





/**
 * @Author Neil Choromokos & Caleb Lemmons
 * Neil: Setup class: Constructors, getters, setters
 * Holden: fixed functionality for frontend
 */
@RestController
public class BikeController {

    @Autowired
    BikeRepository bikeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private GlobalBikeMap globalBikeMap;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //-------------------------- CRUD Operations ------------------// 
    @GetMapping(path = "/bikes")
    List<Bike> getAllBikes(){
        return bikeRepository.findAll();
    }
    @GetMapping(path = "/bikes/{id}")
    Bike getBikeById( @PathVariable int id){
        return bikeRepository.findById(id).orElse(null);
    }


    //Bike takes in User, Color, Name. Bikes are tied to a user's garage
    @PostMapping(path = "/users/{id}/bikes")
    String createBike(@PathVariable int id, @RequestBody Bike bike) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || bike == null) {
            return failure;
        }
        bike.setUser(user);
        //user.getBikes().add(bike);  // Add the bike to the user's list of bikes
        //userRepository.save(user);  // Save the updated user to ensure both sides of relationship are updated
        bikeRepository.save(bike);

        String rentalJson = "{\"alertType\":\"bikeOpen\", \"bikeId\":"+bike.getId()+"}";
        globalBikeMap.broadcast(rentalJson);

        return success;
    }




    @PutMapping("/bikes/{id}")
    public String updateBike(@PathVariable int id, @RequestBody Bike request) {
        Optional<Bike> optionalBike = bikeRepository.findById(id);
        if (!optionalBike.isPresent()) {
            return failure;
        }
        Bike bikeToUpdate = optionalBike.get();

        // Copy attributes from request to bike here
        if (request.getName() != null) {
            bikeToUpdate.setName(request.getName());
        }
        if (request.getPrice() != 0) {
            bikeToUpdate.setPrice(request.getPrice());
        }
        if (request.getLocation() != null) {
            bikeToUpdate.setLocation(request.getLocation());
        }
        if (request.getRating() != null) {
            bikeToUpdate.setRating(request.getRating());
        }
        if(request.getLatitude() != null){
            bikeToUpdate.setLatitude(request.getLatitude());
        }
        if(request.getLongitude() != null){
            bikeToUpdate.setLongitude(request.getLongitude());
        }

        // Save the updated bike
        bikeRepository.save(bikeToUpdate);

        return success;
    }
      
    @DeleteMapping(path = "/bikes/{id}")
    String deleteBike(@PathVariable int id){
        bikeRepository.deleteById(id);
        return success;
    }
}
