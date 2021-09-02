package projekti;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Photo addPhoto(@RequestParam("image") MultipartFile image, @RequestParam String description) throws IOException {
        if (!description.isEmpty() && image.getSize()>0 && image.getContentType().equals("image/jpeg")) {
            Photo photo = new Photo();
            photo.setDescription(description);
            photo.setImage(imageResize(image.getBytes(), 300));
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
    public byte[] getPhoto(@PathVariable Long id, @RequestParam(required = false) Integer height) throws IOException {
        Photo photo = photoService.getPhoto(id);
        if (height != null) {
            return imageResize(photo.getImage(), height);
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
    
    private byte[] imageResize (byte[] image, int height) throws IOException {
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
