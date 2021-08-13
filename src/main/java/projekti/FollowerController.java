package projekti;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowerController {
    @Autowired
    FollowerRepository followerRepository;
    
    @Autowired
    AccountService accountService;
      
    @PostMapping("/follow")
    public Follower addUserToFoollow(@RequestBody Profile profile) {
        Profile follow = accountService.getProfile(profile.getProfilename());
        Profile currentProfile = accountService.getCurrentProfile();
        if (follow != null && !currentProfile.getProfilename().equals(profile.getProfilename())) {
            Follower follower = new Follower();
            follower.setProfile(accountService.getCurrentProfile());
            follower.setFollow(follow);
            follower.setHidden(false);
            return followerRepository.save(follower);
        }
        return null;
    }
    
    @GetMapping("/follow")
    public List<Follower> getUsersFollowed() {
        return followerRepository.findByProfile(accountService.getCurrentProfile());
    }
    
    @GetMapping("/followers")
    public List<Follower> getFollowers() {
        return followerRepository.findByFollow(accountService.getCurrentProfile());
    }
}
