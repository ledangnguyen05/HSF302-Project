package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.UserEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/guestHome")
    public  String guestHomePage(){
        return "user/guestHome";
    }

    @GetMapping("/home")
    public String homePage(HttpSession session) {
        UserEntity userEntity=(UserEntity) session.getAttribute("user");
        if(userEntity==null){
            return "redirect:/login";
        }else{
            if(userEntity.getRole().getRoleName().equals("ADMIN")){
                return "user/adminHome";
            }else if(userEntity.getRole().getRoleName().equals("CUSTOMER")){
                return "user/customerHome";
            }else if(userEntity.getRole().getRoleName().equals("STAFF")){
                return "user/staffHome";
            }
        }
        return "user/guestHome";
    }
}
