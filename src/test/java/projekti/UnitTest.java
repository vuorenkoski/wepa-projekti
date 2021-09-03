package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.account.Account;
import projekti.account.AccountRepository;
import projekti.account.Profile;
import projekti.account.ProfileRepository;
import projekti.follower.FollowerRepository;
import projekti.message.Message;
import projekti.message.MessageComment;
import projekti.message.MessageCommentRepository;
import projekti.message.MessageLike;
import projekti.message.MessageLikeRepository;
import projekti.message.MessageRepository;
import projekti.message.MessageService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UnitTest {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    ProfileRepository profileRepository;
    
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageCommentRepository messageCommentRepository;

    @Autowired
    MessageLikeRepository messageLikeRepository;
    
    @Autowired
    FollowerRepository followerRepository;
    
    @Autowired
    MessageService messageService;
    
    @Before
    public void createUsersAndMessages() {
        Profile p1 = this.createUser("alice", "xxxx", "Alice Smith", "alices");
        Profile p2 = this.createUser("ville", "yyyy", "Ville Virtanen", "viltsu");
        
        Message message = new Message();
        message.setMessage("Tämä on ensimmäinen kirjoitukseni");
        message.setProfile(p1);
        message.setNumberOfLikes(0);
        messageRepository.save(message);
        
        message = new Message();
        message.setMessage("Toinen");
        message.setProfile(p1);
        message.setNumberOfLikes(0);
        message = messageRepository.save(message);
        messageRepository.flush();
        
        MessageComment mc = new MessageComment();
        mc.setComment("hyva");
        messageService.saveMessageComment(mc, message.getId(), p2);
        
        mc = new MessageComment();
        mc.setComment("no jaa...");
        messageService.saveMessageComment(mc, message.getId(), p1);
        messageCommentRepository.flush();
        
        messageService.saveMessageLike(message.getId(), p1);
        messageService.saveMessageLike(message.getId(), p2);
        messageLikeRepository.flush();
    }
    
    @Test
    public void TestAccount() {
        Profile p = profileRepository.findByProfilename("alices");
        assertEquals("Alice Smith", p.getFullname());
    }
    
    @Test
    public void TestFirstMessage() {
        Profile p = profileRepository.findByProfilename("alices");
        List<Profile> profiles = new ArrayList<>();
        profiles.add(p);
        Message m = messageRepository.findByProfileInOrderByDateDesc(profiles).get(1);
        assertEquals("Tämä on ensimmäinen kirjoitukseni", m.getMessage());
    }

    @Test
    public void TestSecondMessage() {
        Profile p = profileRepository.findByProfilename("alices");
        List<Profile> profiles = new ArrayList<>();
        profiles.add(p);
        Message m = messageRepository.findByProfileInOrderByDateDesc(profiles).get(0);
        System.out.println(m.toString());
        assertEquals("Toinen", m.getMessage());
    }
    
    @Test
    public void TestNumberOfLikes() {
        Profile p = profileRepository.findByProfilename("alices");
        List<Profile> profiles = new ArrayList<>();
        profiles.add(p);
        int n = messageRepository.findByProfileInOrderByDateDesc(profiles).get(0).getNumberOfLikes();
        assertEquals(2, n);
    }

    @Test
    public void TestCommentsCount() {
        List <MessageComment> m = messageCommentRepository.findAll();
        assertEquals(2, m.size());
    }
    
    @Test
    public void TestLikesCount() {
        List <MessageLike> m = messageLikeRepository.findAll();
        assertEquals(2, m.size());
    }
    
//    @Test
//    public void TestComments() {
//        Profile p = profileRepository.findByProfilename("alices");
//        List<Profile> profiles = new ArrayList<>();
//        profiles.add(p);
//        Message m = messageRepository.findByProfileInOrderByDateDesc(profiles).get(0);
//        List <MessageComment> mc = messageRepository.getOne(m.getId()).getMessageComments();
//        assertEquals(2, mc.size());
//    }
    
    private Profile createUser(String username, String password, String fullname, String profilename) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        accountRepository.save(account);
        Profile profile = new Profile();
        profile.setFullname(fullname);
        profile.setProfilename(profilename);
        profileRepository.save(profile);
        account.setProfile(profile);
        accountRepository.save(account);
        accountRepository.flush();
        return profile;
    }
}
