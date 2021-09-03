package projekti.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class MessageComment extends AbstractPersistable<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"profile", "message", "date", "messageComments", "numberOfLikes"})
    private Message message;
    
    private String comment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;
    
    private LocalDateTime date = LocalDateTime.now(); 
}
