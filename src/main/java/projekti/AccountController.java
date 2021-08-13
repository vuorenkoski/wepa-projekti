package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup(@ModelAttribute Account account) {
        return "signup";
    }
  
    @GetMapping("/profile")
    public String profile(@ModelAttribute Profile profile) {
        Profile p = accountService.getCurrentAccount().getProfile();
        if (p != null) {
            profile.setFullname(p.getFullname());
            profile.setProfilename(p.getProfilename());
        }
        return "profile";
    }

    @PostMapping("/signup")
    public String addAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        if (accountService.usernameExists(account.getUsername())) {
            bindingResult.rejectValue("username", "error.account", "Käyttäjätunnus on jo käytössä");
            return "signup";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountService.saveAccount(account);
        
        return "redirect:/login";
    }
    
    @PostMapping("/profile")
    public String addProfile(@Valid @ModelAttribute Profile profile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        if (accountService.profilenameExists(profile.getProfilename())) {
            bindingResult.rejectValue("profilename", "error.profile", "Profiilin tunnus on jo käytössä");
            return "profile";
        }
              
        Account account = accountService.getCurrentAccount();
        Profile p = account.getProfile();
        
        if (p == null) {
            profile = accountService.saveProfile(profile); 
            account.setProfile(profile);
            accountService.saveAccount(account);    
        } else {
            p.setFullname(profile.getFullname());
            p.setProfilename(profile.getProfilename());
            accountService.saveProfile(p);
        }
        return "redirect:/";
    }
    
    @GetMapping("/")
    public String index() {
        Account account = accountService.getCurrentAccount();
        if (account != null) {
            if (account.getProfile()!=null) {
                return "redirect:/users/" + account.getProfile().getProfilename();
            }
            return "redirect:/profile";
        }
        return "index";
    }
    
    @GetMapping("users/{profile}")
    public String main(@PathVariable String profile, Model model) {
        Profile currentProfile = accountService.getCurrentProfile();
        if (currentProfile == null || !currentProfile.getProfilename().equals(profile)) {
            return "redirect:/";
        }
        model.addAttribute("user", currentProfile);
        return "mainpage";
    }
}
