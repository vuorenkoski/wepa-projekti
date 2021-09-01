package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>  {

    @EntityGraph(attributePaths = {"messageComments"})
    List<Message> findByProfileOrderByDateDesc(Profile profile);

    @EntityGraph(attributePaths = {"messageComments"})
    List<Message> findByProfileInOrderByDateDesc(List<Profile> profiles);

}
