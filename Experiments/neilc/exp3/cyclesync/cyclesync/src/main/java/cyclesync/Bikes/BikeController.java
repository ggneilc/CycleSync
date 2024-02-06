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
import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Users.User;
import cyclesync.Users.UserRepository;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 */
@RestController
public class BikeController {

    @Autowired
    BikeRepository bikeRepository;
    @Autowired
    UserRepository userRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //-------------------------- CRUD Operations ------------------// 
    @GetMapping(path = "/bikes")
    List<Bike> getAllBikes(){
        return bikeRepository.findAll();
    }
    @GetMapping(path = "/bikes/{id}")
    Bike getBikeById( @PathVariable int id){
        return bikeRepository.findById(id);
    }

    //Bike takes in User, Color, Name. Bikes are tied to a user's garage
    @PostMapping(path = "/users/{id}/bikes")
    String createBike(@PathVariable int id, Bike bike){
        User user = userRepository.findById(id);
        if (bike == null)
            return failure;
        bike.setUser(user);
        bikeRepository.save(bike);
        return success;
    }

    @PutMapping("/bikes/{id}")
    Bike updateBike(@PathVariable int id, @RequestBody Bike request){
        Bike bike = bikeRepository.findById(id);
        if(bike == null)
            return null;
        bikeRepository.save(request);
        return bikeRepository.findById(id);
    } 
      
    @DeleteMapping(path = "/bikes/{id}")
    String deleteBike(@PathVariable int id){
        bikeRepository.deleteById(id);
        return success;
    }
}
