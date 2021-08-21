package projekti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
public class Photo extends AbstractPersistable<Long> {

    @ManyToOne
    private Profile profile;
    
    @NotEmpty
    private String description;
    
    @Lob
    @JsonIgnore
    private byte[] image;
    
    private LocalDateTime date = LocalDateTime.now(); 
    
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    List<PhotoComment> photoComments;

    @JsonIgnore
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    List<PhotoLike> photoLikes;
    
    private int numberOfLikes;
}
