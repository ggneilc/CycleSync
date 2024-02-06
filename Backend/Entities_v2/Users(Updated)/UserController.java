package onetomany.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import onetomany.Bikes.Bike;
import onetomany.Bikes.BikeRepository;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BikeRepository bikeRepository;

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return "User not created: Error";
        userRepository.save(user);
        return user.getName() + " was added!";
    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }   
    
    @PutMapping("/users/{userId}/bikes/{bikeId}")
    String assignBikeToUser(@PathVariable int userId,@PathVariable int bikeId){
        User user = userRepository.findById(userId);
        Bike bike = bikeRepository.findById(bikeId);
        if(user == null || bike == null)
            return "Failed to add bike to user. Please check parameters";
        bike.setUser(user);
        user.setBike(bike);
        userRepository.save(user);
        return "Bike was added to " + user.getName() + "'s inventory.";
    }

    @DeleteMapping(path = "/users/{userId}/bikes/{bikeId}")
    String deleteBike(@PathVariable int id){
        userRepository.deleteById(id);
        return "Bike was removed from inventory.";
    }
}
