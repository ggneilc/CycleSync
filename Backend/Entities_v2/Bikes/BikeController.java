package onetomany.Bikes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BikeController {

    @Autowired
    BikeRepository bikeRepository;

    @GetMapping(path = "/bikes")
    List<Bike> getAllBikes(){
        return bikeRepository.findAll();
    }

    @GetMapping(path = "/bikes/{id}")
    Bike getBikeById( @PathVariable int id){
        return bikeRepository.findById(id);
    }

    @PostMapping(path = "/bikes")
    String createBike(Bike bike){
        if (bike == null)
            return "Bike was not created: Error";
        bikeRepository.save(bike);
        return bike.getManufacturer() + " was added!";
    }

    @PutMapping("/bikes/{id}")
    Bike updateBike(@PathVariable int id, @RequestBody Bike request){
        Bike bike = bikeRepository.findById(id);
        if(bike == null)
            return null;
        bikeRepository.save(request);
        return bikeRepository.findById(id);
    }
}
