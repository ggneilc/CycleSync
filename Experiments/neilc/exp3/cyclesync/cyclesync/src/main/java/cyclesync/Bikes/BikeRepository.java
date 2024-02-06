package cyclesync.Bikes;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @Author Neil Choromokos & Caleb Lemmons
 */
public interface BikeRepository extends JpaRepository<Bike, Long> {
    Bike findById(int id);
    void deleteById(int id);
}
