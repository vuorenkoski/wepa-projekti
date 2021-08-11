package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageComment extends AbstractPersistable<Long> {
    @ManyToOne
    private Message message;
    
    @NotEmpty
    private String comment;
    
    private LocalDateTime date = LocalDateTime.now(); 
}
