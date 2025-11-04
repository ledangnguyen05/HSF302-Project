package hsf302.hsf302project.controller;


import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/guestHome")
    public  String guestHomePage(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
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
