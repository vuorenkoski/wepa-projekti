package projekti.photo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.account.Profile;

public interface PhotoLikeRepository extends JpaRepository<PhotoLike, Long>{
    
    List<PhotoLike> findByProfileAndPhoto(Profile profile, Photo photo);
}
