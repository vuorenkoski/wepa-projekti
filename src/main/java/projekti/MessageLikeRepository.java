package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLikeRepository extends JpaRepository<MessageLike, Long>{
    
    List<MessageLike> findByProfileAndMessage(Profile profile, Message message);
}
