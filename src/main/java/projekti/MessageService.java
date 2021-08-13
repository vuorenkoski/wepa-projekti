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

    @Autowired
    MessageLikeRepository messageLikeRepository;

    
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
    
    public MessageLike saveMessageLike(Long messageid, Profile profile) {
        Message message = this.getMessage(messageid);
        if (messageLikeRepository.findByProfileAndMessage(profile, message).isEmpty()) {
            MessageLike messageLike = new MessageLike();
            messageLike.setMessage(message);
            messageLike.setProfile(profile);
            messageLike = messageLikeRepository.save(messageLike);
            message.setNumberOfLikes(message.getNumberOfLikes() + 1);
            messageRepository.save(message);
            return messageLike;
        }
        return null;
    }
}
