package projekti.message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageCommentRepository extends JpaRepository<MessageComment, Long>  {
    
}
