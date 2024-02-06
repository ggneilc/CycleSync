package cyclesync.Bikes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Neil Choromokos
 *
 */
public interface BikeRepository extends JpaRepository<Bike, Long> {
    Bike findById(int id);
    @Transactional
    void deleteById(int id);
}