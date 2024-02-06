package cyclesync;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import cyclesync.Bikes.Bike;
import cyclesync.Bikes.BikeRepository;
import cyclesync.Users.User;
import cyclesync.Users.UserRepository;
import cyclesync.Rentals.Rental;
import cyclesync.Rentals.RentalRepository;


/**
 * @author Neil Choromokos & Caleb Lemmons
 * @Date 10/02/2023
 */

@SpringBootApplication
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * @param userRepository
     * @param bikeRepository
     * @param rentalRepository
     * @return
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, BikeRepository bikeRepository, RentalRepository rentalRepository) {
        return args -> {
            User tmp1 = new User("Neil", "Choromokos", "test@com", "password!");
            User tmp2 = new User("Thomas", "Payton", "test2@com1", "pa$$word1");
            Bike b1 = new Bike("Generic-Bike-Name", "Regular", "Blue", 10);
            Bike b2 = new Bike("Not-Generic-Name", "ebike", "black", 20);
            Bike b3 = new Bike("Swag", "Regular", " turquiose", 50);
            bikeRepository.save(b1);
            bikeRepository.save(b2);
            bikeRepository.save(b3);
            tmp1.addBike(bikeRepository.findbyId(1));
            tmp2.addBike(bikeRepository.findbyId(2));
            tmp2.addBike(bikeRepository.findbyId(3));
            userRepository.save(tmp1);
            userRepository.save(tmp2);
        };

    }
}
