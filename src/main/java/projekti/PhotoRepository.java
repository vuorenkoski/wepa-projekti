package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @EntityGraph(attributePaths = {"photoComments"})
    List<Message> findByProfileOrderByDateDesc(Profile profile);
}
