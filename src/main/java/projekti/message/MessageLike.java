package projekti.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.account.Profile;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageLike extends AbstractPersistable<Long> {
    @ManyToOne
    @JsonIgnore
    private Message message;
    
    @ManyToOne
    private Profile profile;
    
    private LocalDateTime date = LocalDateTime.now();  
}
