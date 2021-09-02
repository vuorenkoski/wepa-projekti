package projekti.follower;

import projekti.account.Profile;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    List<Follower> findByProfileAndHiddenFalse(Profile profile);

    @EntityGraph(attributePaths = {"follow"})
    List<Follower> findByProfile(Profile profile);
    
    @EntityGraph(attributePaths = {"profile"})
    List<Follower> findByFollow(Profile follow);

    List<Follower> findByProfileAndFollow(Profile profile, Profile follow);
}
