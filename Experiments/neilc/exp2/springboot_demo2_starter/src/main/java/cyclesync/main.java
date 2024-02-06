package cyclesync;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Users.User;
import cyclesync.Users.UserRepository;

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepository, BikeRepository bikeRepository) {
        return args -> {
            User user1 = new User("Caleb L", "caleb@somemail.com", new Date());
            User user2 = new User("Neil G", "neil@somemail.com", new Date());
            User user3 = new User("Thomas S", "thomas@somemail.com", new Date());

            //9 bikes
            bikeRepository.save(new Bike("Some Bike Name", "Red", "New", "Bike #1"));
            bikeRepository.save(new Bike("Some other Bike", "Blue", "Fairly used", "Bike #2"));
            bikeRepository.save(new Bike("Some different Bike", "Purple", "Old", "Bike #3"));
            bikeRepository.save(new Bike("Some altered Bike", "Red", "Old", "Bike #4"));
            bikeRepository.save(new Bike("Some variation of Bike", "Brown", "Barely used", "Bike #5"));
            bikeRepository.save(new Bike("Some eBike", "Black", "New", "Bike #6"));
            bikeRepository.save(new Bike("An eBike", "Blue", "New", "Bike #7"));
            bikeRepository.save(new Bike("The eBike", "White", "Fairly used", "Bike #8"));
            bikeRepository.save(new Bike("Scooter", "White", "Fairly used", "Bike #9"));

            user1.addBike(bikeRepository.findById(1));
            user1.addBike(bikeRepository.findById(2));
            user1.addBike(bikeRepository.findById(3));
            user2.addBike(bikeRepository.findById(4));
            user2.addBike(bikeRepository.findById(5));
            user3.addBike(bikeRepository.findById(6));
            user3.addBike(bikeRepository.findById(7));


            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        };
    }

}