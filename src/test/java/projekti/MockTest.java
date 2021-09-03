package projekti;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import projekti.follower.Follower;
import projekti.follower.FollowerRepository;
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
    FollowerRepository followerRepository;
    
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;
    
    private ObjectMapper mapper;

    @Before
    public void createUsersAndMessages() {
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        
        Profile p1 = this.createUser("alice", "salasana", "Alice Smith", "alices");
        Profile p2 = this.createUser("ville", "salasana", "Ville Virtanen", "viltsu");
        Profile p3 = this.createUser("sami", "salasana", "Sami Seuraaja", "seuraaja");
        Profile p4 = this.createUser("putiini", "salasana", "Putininen trolli", "xkasdit");
        
        Follower follower = new Follower();
        follower.setProfile(p3);
        follower.setFollow(p1);
        follower.setHidden(false);
        followerRepository.save(follower);

        follower = new Follower();
        follower.setProfile(p4);
        follower.setFollow(p1);
        follower.setHidden(true);
        followerRepository.save(follower);
        
        Message message = new Message();
        message.setMessage("Tämä on ensimmäinen kirjoitukseni");
        message.setProfile(p1);
        message.setNumberOfLikes(0);
        messageRepository.save(message);

        message = new Message();
        message.setMessage("Here is ville!");
        message.setProfile(p2);
        message.setNumberOfLikes(0);
        message = messageRepository.save(message);
        
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
        System.out.println("XXXXXXXXXX"+ content);

        Assert.assertTrue(content.contains("kirjoitukseni"));
    }

    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testGetMessagesContainsNumberOfLikes() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();

        String content = res.getResponse().getContentAsString();
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
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testAddNewPersonToFollow() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/profiles?name=ille")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        String id = maplist.get(0).get("id").toString();
        mockMvc.perform(post("/api/follow/" + id)).andExpect(status().is2xxSuccessful());
        
        res = mockMvc.perform(get("/api/follow")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        
        Assert.assertTrue(maplist.get(0).get("follow").toString().contains("Ville Virtanen"));
    } 
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testAddNewPersonToFollowAndRemove() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/profiles?name=ille")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        String id = maplist.get(0).get("id").toString();
        mockMvc.perform(post("/api/follow/" + id)).andExpect(status().is2xxSuccessful());
        
        res = mockMvc.perform(get("/api/follow")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        id = maplist.get(0).get("id").toString();
        Assert.assertTrue(maplist.get(0).get("follow").toString().contains("Ville Virtanen"));
        
        mockMvc.perform(delete("/api/follow/" + id)).andExpect(status().is2xxSuccessful());
        res = mockMvc.perform(get("/api/follow")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        Assert.assertEquals(0, maplist.size());
    } 
   
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testAddNewPersonToFollowAndMessagesAreVisible() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/profiles?name=ille")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        String id = maplist.get(0).get("id").toString();
        mockMvc.perform(post("/api/follow/" + id)).andExpect(status().is2xxSuccessful());
        
        res = mockMvc.perform(get("/api/follow")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        id = maplist.get(0).get("id").toString();
        Assert.assertTrue(maplist.get(0).get("follow").toString().contains("Ville Virtanen"));
        
        res = mockMvc.perform(get("/api/messages")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        Assert.assertEquals(3, maplist.size());
    }
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testFollowers() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/followers")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        String id = maplist.get(0).get("id").toString();
        Assert.assertTrue(maplist.get(0).get("profile").toString().contains("Sami Seuraaja"));
    }

    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testHideFollower() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/followers")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        String id = maplist.get(0).get("id").toString();
        Assert.assertTrue(maplist.get(0).get("profile").toString().contains("Sami Seuraaja"));
        
        mockMvc.perform(post("/api/follower/" + id + "/hide")).andExpect(status().is2xxSuccessful());
        Assert.assertTrue(followerRepository.getOne(Long.parseLong(id)).isHidden());

        mockMvc.perform(post("/api/follower/" + id + "/unhide")).andExpect(status().is2xxSuccessful());
        Assert.assertFalse(followerRepository.getOne(Long.parseLong(id)).isHidden());        
    }
    
    @Test
    @WithMockUser(username = "alice", password = "salasana", roles = "USER")
    public void testUserNumberOfMessage() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));

        res = mockMvc.perform(get("/api/messages")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        Assert.assertEquals(2, maplist.size());
    }

    @Test
    @WithMockUser(username = "sami", password = "salasana", roles = "USER")
    public void testFollowerNumberOfMessage() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));

        res = mockMvc.perform(get("/api/messages")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        Assert.assertEquals(2, maplist.size());
    }

    @Test
    @WithMockUser(username = "putiini", password = "salasana", roles = "USER")
    public void testHiddenFollowerNumberOfMessage() throws Exception {
        MvcResult res = mockMvc.perform(get("/api/messages")).andReturn();
        List<Map> maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));

        res = mockMvc.perform(get("/api/messages")).andReturn();
        maplist = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), Map[].class));
        Assert.assertEquals(0, maplist.size());
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