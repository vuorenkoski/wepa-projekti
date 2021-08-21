package projekti;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PhotoController {
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    PhotoService photoService;
    
    @PostMapping(path="/api/photos")
    public Photo addPhoto(@RequestParam("image") MultipartFile image, @RequestParam String description) throws IOException {
        if (!description.isEmpty() && image.getSize()>0 && image.getContentType().equals("image/jpeg")) {
            Photo photo = new Photo();
            photo.setDescription(description);
            photo.setImage(image.getBytes());
            photo.setProfile(accountService.getCurrentProfile());
            return photoService.savePhoto(photo);
        }
        return null;
    }
    
    @GetMapping("/api/photos")
    public List<Photo> getPhotos() {
        return photoService.getPhotos(accountService.getCurrentProfile());
    }

    @GetMapping(path = "/api/photos/{id}", produces = "image/jpg")
    public byte[] getPhotos(@PathVariable Long id) {
        return photoService.getPhoto(id).getImage();
    }

    @DeleteMapping(path = "/api/photos/{id}", produces = "image/jpg")
    public Photo deletePhoto(@PathVariable Long id) {
        return photoService.deletePhoto(id);
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
    public PhotoLike addLike(@PathVariable Long id) {
        return photoService.savePhotoLike(id, accountService.getCurrentProfile());
    }
    
}
