package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByProfile(Profile profile);
    List<Follower> findByFollow(Profile follow);
    List<Follower> findByProfileAndFollow(Profile profile, Profile follow);
}
