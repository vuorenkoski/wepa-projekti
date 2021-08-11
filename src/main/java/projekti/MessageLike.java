package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageLike {
    @ManyToOne
    private Message message;
    
    @ManyToOne
    private Profile profile;
    
    private LocalDateTime date = LocalDateTime.now();  
}
