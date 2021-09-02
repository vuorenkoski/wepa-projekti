package projekti.message;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.account.Profile;

public interface MessageLikeRepository extends JpaRepository<MessageLike, Long>{
    
    List<MessageLike> findByProfileAndMessage(Profile profile, Message message);
}
