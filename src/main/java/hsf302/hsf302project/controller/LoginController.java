package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                        HttpSession session) {
        UserEntity userEntity = userService.findByUsernameAndPassword(username, password);
        session.setAttribute("user", userEntity);
        if(userEntity!=null){
            return "redirect:/home";
        }
        return "login";
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
