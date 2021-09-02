package projekti;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void statusOk() throws Exception {
    mockMvc.perform(post("/signup")
              .param("username", "alice").param("password", "salasana"))
              .andExpect(redirectedUrl("/login"));
    mockMvc.perform(post("/login")
              .param("username", "alice").param("password", "salasana"))
              .andExpect(redirectedUrl("/"));
//    mockMvc.perform(get("/")).andExpect(redirectedUrl("/profile"));

//    mockMvc.perform(post("/profile")
//              .param("fullname", "Alice Smith").param("profilename", "alicesmith"));
//
//    mockMvc.perform(post("/api/messages")
//              .param("message", "Ensimmäinen viestini")).andExpect(status().is2xxSuccessful());
//
//    mockMvc.perform(post("/api/messages")
//              .param("message", "toinen")).andExpect(status().is2xxSuccessful());
    
  } 
  
}