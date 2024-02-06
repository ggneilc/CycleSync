package cyclesync.Rentals;

import cyclesync.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 * Neil: Setup initial interface
 * Thomas: added optionals
 */

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findById(Integer id);
    void deleteById(int id);
}
