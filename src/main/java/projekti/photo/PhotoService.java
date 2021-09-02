package projekti.photo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import projekti.account.AccountService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekti.ApiError;
import projekti.follower.Follower;
import projekti.follower.FollowerRepository;
import projekti.account.Profile;

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
    public ResponseEntity savePhoto(MultipartFile image, String description) throws IOException {
        if (description.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiError(HttpStatus.BAD_REQUEST, "Kuvaus puuttuu", "invalid format"));
        }
        if (image.getSize() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiError(HttpStatus.BAD_REQUEST, "Kuvaa ei löydy", "invalid format"));
        }
        if (!image.getContentType().equals("image/jpeg")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiError(HttpStatus.BAD_REQUEST, "Kuvan tulee olla jpg muodossa", "invalid format"));
        }
        Profile profile = accountService.getCurrentProfile();
        if (photoRepository.countByProfile(profile) > 9) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ApiError(HttpStatus.FORBIDDEN, "Sinulla voi olla enintään 10 kuvaa. Poista ensin yksi kuva.", "forbidden"));
        }

        Photo photo = new Photo();
        photo.setDescription(description);
        photo.setImage(imageResize(image.getBytes(), 300));
        photo.setProfile(profile);
        photo = photoRepository.save(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

    public ResponseEntity savePhotoComment(PhotoComment photoComment, Long id) {
        if (photoComment.getComment().length()==0 || photoComment.getComment().length()>255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiError(HttpStatus.BAD_REQUEST, "Viestin tulee olla 1-255 merkkiä pitkä", "invalid format"));
        }
        photoComment.setProfile(accountService.getCurrentProfile());
        photoComment.setPhoto(this.getPhoto(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(photoCommentRepository.save(photoComment));
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
    public ResponseEntity deletePhoto(Long id) {
        Profile currentProfile = accountService.getCurrentProfile();
        Photo photo = photoRepository.getOne(id);
        if (!photo.getProfile().equals(currentProfile)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(new ApiError(HttpStatus.UNAUTHORIZED, "Ei oikeutta", "forbidden"));
        }
        if (Objects.equals(currentProfile.getPhoto_id(), photo.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ApiError(HttpStatus.FORBIDDEN, "Kuva on profiilikuvana. Vaihda ensin profiilikuva.", "forbidden"));
        }
        photoRepository.delete(photo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }    
    
    @Transactional
    public ResponseEntity savePhotoLike(Long photoid, Profile profile) {
        Photo photo = this.getPhoto(photoid);
        if (!photoLikeRepository.findByProfileAndPhoto(profile, photo).isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ApiError(HttpStatus.FORBIDDEN, "Olet jo tykännyt kuvasta", "forbidden"));
        }
        PhotoLike photoLike = new PhotoLike();
        photoLike.setPhoto(photo);
        photoLike.setProfile(profile);
        photoLike = photoLikeRepository.save(photoLike);
        photo.setNumberOfLikes(photo.getNumberOfLikes() + 1);
        photoRepository.save(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(photoLike.getPhoto());
    }
    
    public byte[] imageResize (byte[] image, int height) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(image);
        BufferedImage bImage = ImageIO.read(bis);
        
        if (bImage.getHeight()>height) {
            int width = ((height * bImage.getWidth()) / bImage.getHeight());
            Image resultingImage = bImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "jpg", bos);

            return bos.toByteArray();           
        }
        return image;
    } 
}