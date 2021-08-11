package projekti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message extends AbstractPersistable<Long> {
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;
    
    @NotEmpty
    private String message;
    
    private LocalDateTime date = LocalDateTime.now(); 
    
    @OneToMany(mappedBy = "message")
    List<MessageComment> messageComments;
    
    @OneToMany(mappedBy = "message")
    List<MessageLike> messageLikes;
}
