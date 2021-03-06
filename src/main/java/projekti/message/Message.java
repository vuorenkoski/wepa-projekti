package projekti.message;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.account.Profile;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Profile profile;
    
    @NotEmpty
    @Column(columnDefinition="TEXT")
    private String message;
    
    private LocalDateTime date = LocalDateTime.now(); 
    
    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER)
    List<MessageComment> messageComments;
    
    private int numberOfLikes;
}
