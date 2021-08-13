package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Follower extends AbstractPersistable<Long> {

    @ManyToOne
    private Profile profile;
    
    @ManyToOne
    private Profile follow;
    
    private LocalDateTime date = LocalDateTime.now();    
    private boolean hidden;
}
