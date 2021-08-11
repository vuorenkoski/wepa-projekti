package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    MessageService messageService;
    
    @PostMapping("/messages")
    public Message addMessage(@RequestBody Message message) {
        message.setProfile(accountService.getCurrentProfile());
        message = messageService.saveMessage(message);
        return message;
    }
    
    @GetMapping("/users/{profile}/messages")
    public List<Message> getMessages(@PathVariable String profile) {
        return messageService.getMessages(accountService.getProfile(profile));
    }
    
}
