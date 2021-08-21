package projekti;

import java.util.List;
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
        Account acc = accountRepository.findByUsername(auth.getName());
        if (acc == null) {
            return null;
        }
        return acc.getProfile();
    }
    
    public boolean profileBelongsToCurrentAccount (String profilename) {
        Profile p = profileRepository.findByProfilename(profilename);
        return p.equals(this.getCurrentProfile());
    }
      
    public Profile getProfileById (Long id) {
        return profileRepository.getOne(id);
    }
    
    public List<Profile> searchProfiles (String searchTerm) {
        List<Profile> profiles = profileRepository.findByFullnameContainingIgnoreCase(searchTerm);
        return profiles;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
    
    public Profile saveProfile(Profile profile) {
        profile = profileRepository.save(profile);
        profileRepository.flush();
        return profile;
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
