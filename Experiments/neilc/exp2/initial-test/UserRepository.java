package cyclesync.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Neil Choromokos
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    @Transactional
    void deleteById(int id);
}