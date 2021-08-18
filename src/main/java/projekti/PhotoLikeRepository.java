package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoLikeRepository extends JpaRepository<PhotoLike, Long>{
    
    List<PhotoLike> findByProfileAndPhoto(Profile profile, Photo photo);
}
