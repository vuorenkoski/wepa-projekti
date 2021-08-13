package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageCommentRepository messageCommentRepository;

    
    public Message saveMessage(Message message) {
        message = messageRepository.save(message);
        return messageRepository.save(message);
    }

    public MessageComment saveMessageComment(MessageComment messageComment) {
        messageComment = messageCommentRepository.save(messageComment);
        return messageCommentRepository.save(messageComment);
    }
    
    public List<Message> getMessages(Profile profile) {
        return messageRepository.findByProfileOrderByDateDesc(profile);
    }
    
    public Message getMessage(Long id) {
        return messageRepository.getOne(id);
    }
}
