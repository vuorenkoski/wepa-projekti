package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    ProfileRepository profileRepository;
       
    public Account getCurrentAccount () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return accountRepository.findByUsername(auth.getName());
    }
    
    public Profile getCurrentProfile () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return accountRepository.findByUsername(auth.getName()).getProfile();
    }
    
    public boolean profileBelongsToCurrentAccount (String profilename) {
        Profile p = profileRepository.findByProfilename(profilename);
        return p.equals(this.getCurrentProfile());
    }
    
    public Profile getProfile (String profilename) {
        return profileRepository.findByProfilename(profilename);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
    
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
    
    public boolean usernameExists(String username) {
        return !(accountRepository.findByUsername(username) == null);
    }
    
    public boolean profilenameExists(String profilename) {
        Profile current = this.getCurrentProfile();
        if ((current != null) && profilename.equals(current.getProfilename())) {
            return false;
        }
        return !(profileRepository.findByProfilename(profilename) == null);
    }
}
