package projekti.account;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account extends AbstractPersistable<Long> {

    @NotEmpty(message = "ei voi olla tyhjä")
    @Size(min = 3, max = 11, message = "3-11 merkkiä")
    private String username;

    @NotEmpty(message = "ei voi olla tyhjä")
    private String password;

    @OneToOne
    private Profile profile;
}
