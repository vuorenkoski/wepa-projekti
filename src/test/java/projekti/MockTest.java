package projekti;

import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import projekti.account.Account;
import projekti.account.AccountRepository;
import projekti.account.Profile;
import projekti.account.ProfileRepository;
import projekti.message.Message;
import projekti.message.MessageComment;
import projekti.message.MessageCommentRepository;
import projekti.message.MessageLikeRepository;
import projekti.message.MessageRepository;
import projekti.message.MessageService;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MockTest {
    
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
    MessageService messageService;
    
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void createUsersAndMessages() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        
        Profile p1 = this.createUser("alice", "salasana", "Alice Smith", "alices");
        Profile p2 = this.createUser("ville", "salasana", "Ville Virtanen", "viltsu");
        
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
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testAddProfile() throws Exception {
        mockMvc.perform(post("/profile").with(csrf())
                .param("fullname", "Alice Smith").param("profilename", "alicesmith"));
        Profile profile = profileRepository.findByProfilename("alicesmith");
        Assert.assertNotNull(profile);
    }
      
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testGetMessagesReturnsOk() throws Exception {
        mockMvc.perform(get("/api/messages")).andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/users/alices")).andExpect(status().is2xxSuccessful());
    }
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testGetMessagesContainsMessage() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();

        String content = res.getResponse().getContentAsString();
        Assert.assertTrue(content.contains("kirjoitukseni"));
  }

    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testGetMessagesContainsNumberOfLikes() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();

        String content = res.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertTrue(content.contains("\"numberOfLikes\":2"));
    }    
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testNewMessage() throws Exception {
        mockMvc.perform(post("/api/messages").contentType("application/json").content("{\"message\":\"kolmas viestini\"}")).andExpect(status().is2xxSuccessful());
        
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();

        String content = res.getResponse().getContentAsString();
        Assert.assertTrue(content.contains("kolmas"));
    } 
    
        
    private projekti.account.Profile createUser(String username, String password, String fullname, String profilename) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        accountRepository.save(account);
        projekti.account.Profile profile = new projekti.account.Profile();
        profile.setFullname(fullname);
        profile.setProfilename(profilename);
        profileRepository.save(profile);
        account.setProfile(profile);
        accountRepository.save(account);
        accountRepository.flush();
        return profile;
    }
  
}