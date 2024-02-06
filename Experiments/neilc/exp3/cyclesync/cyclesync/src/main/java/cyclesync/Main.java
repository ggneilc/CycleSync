package cyclesync;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cyclesync.Rentals.Rental;
import cyclesync.Rentals.RentalRepository;
import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Users.User;
import cyclesync.Users.UserRepository;

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their Bikes and Rentals
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, RentalRepository rentalRepository, BikeRepository bikeRepository) {
        return args -> {
            /*
          User user1 = new User("Neil", "Choromokos", "nchurro@mail.com", "password1");
          User user2 = new User("Thomas", "Payton", "tpayton@mail.com", "testpass1");
          Rental rental1 = new Rental(user1, 20);
          Rental rental2 = new Rental(user1, 30);
          Rental rental3 = new Rental(user2, 15);
          bikeRepository.save(new Bike(user1, "Gray", "Skootr 2"));
          bikeRepository.save(new Bike(user1, "Black", "Skootr 5"));
          userRepository.save(user1);
          userRepository.save(user2);
          */

            /*
            User user1 = new User("Neil", "nchurror", "test", "nchurro@somemail.com");
            User user2 = new User("Caleb", "lemmon", "test", "clemmons@somemail.com");
            User user3 = new User("Thomas", "tpayton", "test", "tpayton@somemail.com");
            Rental r1 = new Rental( "Current", 15, new Date());
            Rental r2 = new Rental( "Past", 18, new Date());
            Rental r3 = new Rental( "Upcoming", 0, new Date());

            bikeRepository.save(ne Bike("Tesla", "New", "Gray", "Skootr 2", 99 ));
            bikeRepository.save(new Bike("Tesla", "Used", "White", "Skootr", 50 ));
            bikeRepository.save(new Bike("Razer", "New", "Green", "Cool Bike name", 99 ));
            bikeRepository.save(new Bike("Razer", "Battered", "Black", "terrain destroyer", 99 ));
            bikeRepository.save(new Bike("Mountaineer", "Old", "Blue", "swag bike", 99 ));
            bikeRepository.save(new Bike("Mountaineer", "Worn", "Purple", "bicyle", 99 ));
            bikeRepository.save(new Bike("HotWheels", "New", "Yellow", "epic bicyle", 99 ));

            user1.setRental(r1);
            user2.setRental(r2);
            user3.setRental(r3);
            user1.addBikes(bikeRepository.findById(1));
            user1.addBikes(bikeRepository.findById(2));            
            user1.addBikes(bikeRepository.findById(6));
            user2.addBikes(bikeRepository.findById(3));
            user2.addBikes(bikeRepository.findById(4)); 
            user3.addBikes(bikeRepository.findById(5));
            user3.addBikes(bikeRepository.findById(7));
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);*/
        };
    }

}
