package cyclesync.Users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Neil Choromokos & Caleb Lemmons
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    User findByEmailId(String id);

    void deleteById(int id);
}
