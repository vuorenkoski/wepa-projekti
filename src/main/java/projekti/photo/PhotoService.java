package projekti.photo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import projekti.account.AccountService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Kuvaus puuttuu", "invalid format"), HttpStatus.BAD_REQUEST);
        }
        if (image.getSize() == 0) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Kuvaa ei löydy", "invalid format"), HttpStatus.BAD_REQUEST);
        }
        if (!image.getContentType().equals("image/jpeg")) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Kuvan tulee olla jpg muodossa", "invalid format"), HttpStatus.BAD_REQUEST);
        }
        Profile profile = accountService.getCurrentProfile();
        if (photoRepository.countByProfile(profile) > 9) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Sinulla voi olla enintään 10 kuvaa", "db error"), HttpStatus.BAD_REQUEST);
        }

        Photo photo = new Photo();
        photo.setDescription(description);
        photo.setImage(imageResize(image.getBytes(), 300));
        photo.setProfile(profile);
        photo = photoRepository.save(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
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