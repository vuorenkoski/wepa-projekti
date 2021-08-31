package projekti;

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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhotoComment extends AbstractPersistable<Long> {

    @ManyToOne
    @JsonIgnoreProperties({"profile", "description", "image", "date", "photoComments", "photoLikes", "numberOfLikes" })
    private Photo photo;
    
    private String comment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;
    
    private LocalDateTime date = LocalDateTime.now(); 
}