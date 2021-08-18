package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoCommentRepository extends JpaRepository<PhotoComment, Long>  {
    
}