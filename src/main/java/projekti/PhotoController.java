package projekti;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    PhotoService photoService;
    
    @PostMapping(path="/api/photos")
    public String addPhoto(@RequestParam("image") MultipartFile image, @RequestParam String description, Model model) {
        System.out.println(image.getSize());
        System.out.println(description);
        System.out.println(image.getContentType());
        model.addAttribute("user", accountService.getCurrentProfile());
        model.addAttribute("defaultTab", "photo");
        return "mainpage";
    }
    
    @ResponseBody
    @GetMapping("/api/photos")
    public List<Photo> getPhotos() {
        return photoService.getPhotos(accountService.getCurrentProfile());
    }
    
    @ResponseBody
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

    @ResponseBody
    @PostMapping("/api/photos/{id}/likes")
    public PhotoLike addLike(@PathVariable Long id) {
        return photoService.savePhotoLike(id, accountService.getCurrentProfile());
    }
    
}
