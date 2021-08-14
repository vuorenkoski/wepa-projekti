package projekti;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @DeleteMapping("/follow/{id}")
    public void deleteFollow(@PathVariable Long id) {
        Profile profile = accountService.getCurrentProfile();
        Follower follower = followerRepository.getOne(id);
        if (profile.equals(follower.getProfile())) {
            followerRepository.delete(follower);
        }
    }
    
    @GetMapping("/followers")
    public List<Follower> getFollowers() {
        return followerRepository.findByFollow(accountService.getCurrentProfile());
    }
    
    @PostMapping("/follower/{id}/hide")
    public void hideFollower(@PathVariable Long id) {
        Profile follow = accountService.getCurrentProfile();
        Follower follower = followerRepository.getOne(id);
        if (follow.equals(follower.getFollow())) {
            follower.setHidden(true);
            followerRepository.save(follower);
        }
    }

    @PostMapping("/follower/{id}/unhide")
    public void unhideFollower(@PathVariable Long id) {
        Profile follow = accountService.getCurrentProfile();
        Follower follower = followerRepository.getOne(id);
        if (follow.equals(follower.getFollow())) {
            follower.setHidden(false);
            followerRepository.save(follower);
        }
    }
}
