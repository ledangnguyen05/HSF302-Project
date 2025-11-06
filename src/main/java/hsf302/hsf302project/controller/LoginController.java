package hsf302.hsf302project.controller;

import ch.qos.logback.core.model.Model;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        UserEntity userEntity = userService.findByUsernameAndPassword(username, password);
        session.setAttribute("user", userEntity);
        if(userEntity!=null && userEntity.getActive()){
            return "redirect:/home";
        }
        else if (userEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Your account is inactive. Please contact support.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/guestHome";
    }

    @GetMapping("/")
    public String guestPage(HttpSession session) {
        session.invalidate();
        return "redirect:/guestHome";
    }
}
