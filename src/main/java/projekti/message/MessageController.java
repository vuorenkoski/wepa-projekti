package projekti.message;

import projekti.account.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity addMessage(@RequestBody Message message) {
        return messageService.saveMessage(message, accountService.getCurrentProfile());
    }
    
    @GetMapping("/api/messages")
    public List<Message> getMessages() {
        return messageService.getMessages(accountService.getCurrentProfile());
    }
    
    @PostMapping("/api/messages/{id}/comments")
    public ResponseEntity addComment(@RequestBody MessageComment messageComment, @PathVariable Long id) {
        return  messageService.saveMessageComment(messageComment, id, accountService.getCurrentProfile());
    }

    @PostMapping("/api/messages/{id}/likes")
    public ResponseEntity addLike(@PathVariable Long id) {
        return messageService.saveMessageLike(id, accountService.getCurrentProfile());
    }
    
}
