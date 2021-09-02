package projekti.message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projekti.ApiError;
import projekti.account.AccountService;
import projekti.follower.FollowerRepository;
import projekti.account.Profile;

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
    
    @Autowired
    AccountService accountService;
    
    public ResponseEntity saveMessage(Message message) {
        if ((message.getMessage().length() == 0) || (message.getMessage().length() > 1023)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiError(HttpStatus.BAD_REQUEST, "Viestin tulee olla 1-1023 merkkiä pitkä", "invalid format"));
        }

        message.setProfile(accountService.getCurrentProfile());
        message.setNumberOfLikes(0);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageRepository.save(message));
    }

    public ResponseEntity saveMessageComment(MessageComment messageComment, Long id) {
        if (messageComment.getComment().length()==0 || messageComment.getComment().length()>255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiError(HttpStatus.BAD_REQUEST, "Viestin tulee olla 1-255 merkkiä pitkä", "invalid format"));
        }
        messageComment.setProfile(accountService.getCurrentProfile());
        messageComment.setMessage(this.getMessage(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(messageCommentRepository.save(messageComment));
    }
    
    public List<Message> getMessages(Profile profile) {
        List<Profile> profiles = followerRepository.findByProfileAndHiddenFalse(profile).stream()
                .map(x -> x.getFollow()).collect(Collectors.toList());
        profiles.add(profile);
        return messageRepository.findByProfileInOrderByDateDesc(profiles).stream().limit(25).collect(Collectors.toList());
    }
    
    public Message getMessage(Long id) {
        return messageRepository.getOne(id);
    }
    
    public ResponseEntity saveMessageLike(Long messageid, Profile profile) {
        Message message = this.getMessage(messageid);
        if (!messageLikeRepository.findByProfileAndMessage(profile, message).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ApiError(HttpStatus.FORBIDDEN, "Olet jo tykännyt viestistä", "forbidden"));
        }
        MessageLike messageLike = new MessageLike();
        messageLike.setMessage(message);
        messageLike.setProfile(profile);
        messageLike = messageLikeRepository.save(messageLike);
        message.setNumberOfLikes(message.getNumberOfLikes() + 1);
        messageRepository.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageLike.getMessage());
    }
}
