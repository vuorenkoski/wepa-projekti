package projekti.photo;

import projekti.account.AccountService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PhotoController {
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    PhotoService photoService;
    
    @PostMapping(path="/api/photos")
    public ResponseEntity addPhoto(@RequestParam("image") MultipartFile image, @RequestParam String description) throws IOException {
        return photoService.savePhoto(image, description);
    }
    
    @GetMapping("/api/photos")
    public List<Photo> getPhotos() {
        return photoService.getPhotos(accountService.getCurrentProfile());
    }

    @GetMapping(path = "/api/photos/{id}", produces = "image/jpg")
    public byte[] getPhoto(@PathVariable Long id, @RequestParam(required = false) Integer height) throws IOException {
        Photo photo = photoService.getPhoto(id);
        if (height != null) {
            return photoService.imageResize(photo.getImage(), height);
        }
        return photo.getImage();
    }

    @DeleteMapping(path = "/api/photos/{id}")
    public void deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
    }
    
    @PostMapping("/api/photos/{id}/comments")
    public PhotoComment addComment(@RequestBody PhotoComment photoComment, @PathVariable Long id) {
        if (photoComment.getComment().length()>0 && photoComment.getComment().length()<255) {
            photoComment.setProfile(accountService.getCurrentProfile());
            photoComment.setPhoto(photoService.getPhoto(id));
            photoComment = photoService.savePhotoComment(photoComment);
            return photoComment;
        }
        return null;
    }
   
    @PostMapping("/api/photos/{id}/likes")
    public Photo addLike(@PathVariable Long id) {
        return photoService.savePhotoLike(id, accountService.getCurrentProfile()).getPhoto();
    }
}
