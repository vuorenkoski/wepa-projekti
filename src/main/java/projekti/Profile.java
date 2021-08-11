package projekti;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Profile extends AbstractPersistable<Long> {
    @NotEmpty
    @Size(min = 8, max = 30)
    private String fullname;

    @NotEmpty
    @Size(min = 5, max = 20)
    @Pattern(regexp = "[a-z]*")
    private String profilename;
}
