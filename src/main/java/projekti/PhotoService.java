package projekti;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoCommentRepository photoCommentRepository;

    @Autowired
    PhotoLikeRepository photoLikeRepository;
    
    @Autowired
    FollowerRepository followerRepository;

    @Autowired
    AccountService accountService;
    
    @Transactional
    public Photo savePhoto(Photo photo) {
        if (photoRepository.countByProfile(photo.getProfile()) < 10) {
            return photoRepository.save(photo);
        }
        return null;
    }

    public PhotoComment savePhotoComment(PhotoComment photoComment) {
        return photoCommentRepository.save(photoComment);
    }
    
    @Transactional
    public List<Photo> getPhotos(Profile profile) {
        List<Profile> profiles = followerRepository.findByProfileAndHiddenFalse(profile).stream()
                .map(x -> x.getFollow()).collect(Collectors.toList());
        profiles.add(profile);
        return photoRepository.findByProfileInOrderByDateDesc(profiles).stream().limit(25).collect(Collectors.toList());
    }
    
    @Transactional
    public Photo getPhoto(Long id) {
        Photo photo = photoRepository.getOne(id);
        Profile profile = accountService.getCurrentProfile();
        if (photo.getProfile().equals(profile)) {
            return photo;
        }
        List<Follower> follower = followerRepository.findByProfileAndFollow(profile, photo.getProfile());
        if (!follower.isEmpty() && !follower.get(0).isHidden()) {
           return photo; 
        }
        return null;
    }
    
    @Transactional
    public void deletePhoto(Long id) {
        Profile currentProfile = accountService.getCurrentProfile();
        Photo photo = photoRepository.getOne(id);
        if (!photo.getProfile().equals(currentProfile)) {
            return;
        }
        if (Objects.equals(currentProfile.getPhoto_id(), photo.getId())) {
            return;
        }
        photoRepository.delete(photo);
    }    
    
    @Transactional
    public PhotoLike savePhotoLike(Long photoid, Profile profile) {
        Photo photo = this.getPhoto(photoid);
        if (photoLikeRepository.findByProfileAndPhoto(profile, photo).isEmpty()) {
            PhotoLike photoLike = new PhotoLike();
            photoLike.setPhoto(photo);
            photoLike.setProfile(profile);
            photoLike = photoLikeRepository.save(photoLike);
            photo.setNumberOfLikes(photo.getNumberOfLikes() + 1);
            photoRepository.save(photo);
            return photoLike;
        }
        return null;
    }
}