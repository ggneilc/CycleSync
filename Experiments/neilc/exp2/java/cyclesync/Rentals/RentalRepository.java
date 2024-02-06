package cyclesync.Rentals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Neil Choromokos
 *
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {
    Rental findById(int id);
    @Transactional
    void deleteById(int id);
}