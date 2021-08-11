package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    
    public Message saveMessage(Message message) {
        message = messageRepository.save(message);
        return messageRepository.save(message);
    }
    
    public List<Message> getMessages(Profile profile) {
        return messageRepository.findByProfile(profile);
    }
}
