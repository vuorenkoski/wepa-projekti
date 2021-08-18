package projekti;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    
    @Autowired
    FollowerRepository followerRepository;
    
    public Message saveMessage(Message message) {
        message = messageRepository.save(message);
        return messageRepository.save(message);
    }

    public MessageComment saveMessageComment(MessageComment messageComment) {
        messageComment = messageCommentRepository.save(messageComment);
        return messageCommentRepository.save(messageComment);
    }
    
    public List<Message> getMessages(Profile profile) {
        List<Message> messages = new ArrayList<>();
        followerRepository.findByProfile(profile).stream()
                .filter(x -> !x.isHidden()).map(x -> x.getFollow()).
                map(x -> x.getMessages()).forEach(messages::addAll);
        messages.addAll(messageRepository.findByProfileOrderByDateDesc(profile));
        return messages.stream().sorted(Comparator.comparing(Message::getDate).reversed()).limit(25).collect(Collectors.toList());
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
