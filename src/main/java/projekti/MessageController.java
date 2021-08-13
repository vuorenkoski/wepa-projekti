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
        if (message.getMessage().length()>0 && message.getMessage().length()<255) {
            message.setProfile(accountService.getCurrentProfile());
            message = messageService.saveMessage(message);
            return message;
        }
        return null;
    }
    
    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageService.getMessages(accountService.getCurrentProfile());
    }
    
    @PostMapping("/messages/{id}/comments")
    public MessageComment addcomment(@RequestBody MessageComment messageComment, @PathVariable Long id) {
        if (messageComment.getComment().length()>0 && messageComment.getComment().length()<255) {
            messageComment.setProfile(accountService.getCurrentProfile());
            messageComment.setMessage(messageService.getMessage(id));
            messageComment = messageService.saveMessageComment(messageComment);
            return messageComment;
        }
        return null;
    }
    
}
