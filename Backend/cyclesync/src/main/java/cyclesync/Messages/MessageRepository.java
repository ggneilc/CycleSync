package cyclesync.Messages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findById(Integer id);

    List<Message> findBySenderIdAndReceiverId(int senderId, int receiverId);
}
