package projekti.message;

import projekti.account.AccountService;
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
    
    @PostMapping("/api/messages")
    public Message addMessage(@RequestBody Message message) {
        if (message.getMessage().length()>0 && message.getMessage().length()<1023) {
            message.setProfile(accountService.getCurrentProfile());
            message.setNumberOfLikes(0);
            message = messageService.saveMessage(message);
            return message;
        }
        return null;
    }
    
    @GetMapping("/api/messages")
    public List<Message> getMessages() {
        return messageService.getMessages(accountService.getCurrentProfile());
    }
    
    @PostMapping("/api/messages/{id}/comments")
    public MessageComment addComment(@RequestBody MessageComment messageComment, @PathVariable Long id) {
        if (messageComment.getComment().length()>0 && messageComment.getComment().length()<255) {
            messageComment.setProfile(accountService.getCurrentProfile());
            messageComment.setMessage(messageService.getMessage(id));
            messageComment = messageService.saveMessageComment(messageComment);
            return messageComment;
        }
        return null;
    }

    @PostMapping("/api/messages/{id}/likes")
    public MessageLike addLike(@PathVariable Long id) {
        return messageService.saveMessageLike(id, accountService.getCurrentProfile());
    }
    
}
