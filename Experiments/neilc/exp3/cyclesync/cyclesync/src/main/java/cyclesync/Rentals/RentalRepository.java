package cyclesync.Rentals;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 */

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Rental findById(int id);
    void deleteById(int id);
}
