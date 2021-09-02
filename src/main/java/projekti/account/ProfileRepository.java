package projekti.account;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByProfilename(String profilename);
    
    List<Profile> findByFullnameContainingIgnoreCase(String fullname);
}
