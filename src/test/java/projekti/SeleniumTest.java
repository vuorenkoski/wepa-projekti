package projekti;

import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class SeleniumTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;
    
    private String address = "http://localhost:";
   
    @Test
    public void testTooShortUsername() throws InterruptedException {
        goTo(address + port + "/signup");
        find("#username").fill().with("b");
        find("#password").fill().with("salasana");
        find("form").first().submit();
        assertTrue(pageSource().contains("3-11 merkkiä"));
    }
    
    @Test
    public void testIllegalLettersInProfilename() throws InterruptedException {
        goTo(address + port + "/signup");
        find("#username").fill().with("bobob");
        find("#password").fill().with("salasana");
        find("form").first().submit();
        assertTrue(pageSource().contains("Kirjaudu sisään"));
        find("#username").fill().with("bobob");
        find("#password").fill().with("salasana");
        find("form").first().submit();
        assertTrue(pageSource().contains("Ennen palvelun käyttämistä"));
        find("#fullname").fill().with("Bob Marley");
        find("#profilename").fill().with("bob bob");
        find("form").first().submit();
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains("voi sisältää vain merkkejä a-z"));
    }
    
    @Test
    public void testCanSignUpAndPublishMessage() throws InterruptedException {
        signUpAndSendMessage("bob", "Bob Marley", "wailersbob", "Buffalo Soldier, Dreadlock Rasta");
        find("#logout").click();
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains("Luo uusi käyttäjätunnus tai kirjaudu sisään"));

    }

    @Test
    public void testCanSignUpAndFollowUserAndLikeAndComment() throws InterruptedException {
        signUpAndSendMessage("roope", "Roope Ankka", "kroisos", "Missä Aku on?");
        find("#logout").click();
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains("Luo uusi käyttäjätunnus tai kirjaudu sisään"));

        signUpAndSendMessage("donald", "Donald Trump", "realtrump", "I think if this country gets any kinder or gentler, it's literally going to cease to exist.");
        find("#followersTab").click();
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains("Seurattavat käyttäjät"));
        find("#profileName").fill().with("Roope Ankka");
        find("#searchProfiles").click();
        TimeUnit.MILLISECONDS.sleep(200);
        find("#followUserClick").first().click();
        TimeUnit.MILLISECONDS.sleep(100);
        find("#messageTab").click();
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains("Missä Aku on?"));
        assertTrue(pageSource().contains("kinder or gentler"));
        assertFalse(pageSource().contains("1 tykkäystä"));
        find("#likeButton").first().click();
        TimeUnit.MILLISECONDS.sleep(300);
        assertTrue(pageSource().contains("1 tykkäystä"));
        find("textarea").last().fill().with("Jaaha?");
        find("#commentButton").last().click();
        TimeUnit.MILLISECONDS.sleep(100);
        goTo(address + port + "/users/realtrump");
        find("#followersTab").click();
        TimeUnit.MILLISECONDS.sleep(100);
        find("#messageTab").click();

        TimeUnit.MILLISECONDS.sleep(300);
        assertTrue(pageSource().contains("Jaaha?"));
    }
    
    private void signUpAndSendMessage(String username, String fullname, String profilename, String message) throws InterruptedException {
        goTo(address + port + "/signup");
        find("#username").fill().with(username);
        find("#password").fill().with("salasana");
        find("form").first().submit();
        assertTrue(pageSource().contains("Kirjaudu sisään"));
        find("#username").fill().with(username);
        find("#password").fill().with("salasana");
        find("form").first().submit();
        assertTrue(pageSource().contains("Ennen palvelun käyttämistä"));
        find("#fullname").fill().with(fullname);
        find("#profilename").fill().with(profilename);
        find("form").first().submit();
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains("Julkaise uusi viesti"));
        find("#newMessage").fill().with(message);
        find("#publishButton").click();
        TimeUnit.MILLISECONDS.sleep(100);
        goTo(address + port + "/users/" + profilename);
        TimeUnit.MILLISECONDS.sleep(100);
        assertTrue(pageSource().contains(message));
    }
}
