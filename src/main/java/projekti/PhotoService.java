package projekti;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    
    public Photo savePhoto(Photo photo) {
        if (photoRepository.countByProfile(photo.getProfile()) < 10) {
            return photoRepository.save(photo);
        }
        return null;
    }

    public PhotoComment savePhotoComment(PhotoComment photoComment) {
        return photoCommentRepository.save(photoComment);
    }
    
    public List<Photo> getPhotos(Profile profile) {
        List<Photo> photos = new ArrayList<>();
        followerRepository.findByProfile(profile).stream()
                .filter(x -> !x.isHidden()).map(x -> x.getFollow()).
                map(x -> x.getPhotos()).forEach(photos::addAll);
        photos.addAll(photoRepository.findByProfileOrderByDateDesc(profile));
        return photos.stream().sorted(Comparator.comparing(Photo::getDate).reversed()).limit(25).collect(Collectors.toList());
    }
    
    public Photo getPhoto(Long id) {
        return photoRepository.getOne(id);
    }
    
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