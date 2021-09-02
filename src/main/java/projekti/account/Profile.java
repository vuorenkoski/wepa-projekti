package projekti.account;

import projekti.photo.Photo;
import projekti.message.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.follower.Follower;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Profile extends AbstractPersistable<Long> {
    @NotEmpty(message = "ei voi olla tyhjä")
    @Size(min = 8, max = 30, message = "8-30 merkkiä")
    private String fullname;

    @NotEmpty(message = "ei voi olla tyhjä")
    @Size(min = 5, max = 20, message = "5-20 merkkiä")
    @Pattern(regexp = "[a-z]*", message = "voi sisältää vain merkkejä a-z")
    private String profilename;
    
    private Long photo_id;
    
    @JsonIgnore
    @OneToMany(mappedBy = "profile")
    private List<Follower> follows;
    
    @JsonIgnore
    @OneToMany(mappedBy = "profile")
    private List<Message> messages;

    @JsonIgnore
    @OneToMany(mappedBy = "profile")
    private List<Photo> photos;
}
