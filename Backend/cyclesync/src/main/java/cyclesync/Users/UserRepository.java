package cyclesync.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    User findByEmailId(String id);

    void deleteById(int id);
}
