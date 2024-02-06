package cyclesync.Bikes;

import cyclesync.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 * Caleb: set up initial interface
 * Thomas: added optional usage
 */
public interface BikeRepository extends JpaRepository<Bike, Integer> {
    Optional<Bike> findById(Integer id);
    void deleteById(int id);
}
