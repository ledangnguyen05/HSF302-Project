package hsf302.hsf302project.controller;


import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.service.CategoryService;
import hsf302.hsf302project.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/guestHome")
    public  String guestHomePage(@RequestParam(required = false) Integer categoryId, Model model) {
        List<ProductEntity> products;
        if (categoryId != null && categoryId > 0) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.getAllProducts();
        }
        List<CategoryEntity> categories = categoryService.findAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId != null ? categoryId : 0);
        return "user/guestHome";
    }

    @GetMapping("/home")
    public String homePage(@RequestParam(required = false) Integer categoryId, HttpSession session, Model model) {
        UserEntity userEntity=(UserEntity) session.getAttribute("user");
        if(userEntity==null){
            return "redirect:/login";
        }else{
            if(userEntity.getRole().getRoleName().equals("ADMIN")){
                return "user/adminHome";
            }else if(userEntity.getRole().getRoleName().equals("CUSTOMER")){
                List<ProductEntity> products;
                if (categoryId != null && categoryId > 0) {
                    products = productService.findByCategoryId(categoryId);
                } else {
                    products = productService.getAllProducts();
                }
                List<CategoryEntity> categories = categoryService.findAllCategories();
                model.addAttribute("products", products);
                model.addAttribute("categories", categories);
                model.addAttribute("selectedCategoryId", categoryId != null ? categoryId : 0);
                return "user/customerHome";
            }else if(userEntity.getRole().getRoleName().equals("STAFF")){
                return "user/staffHome";
            }
        }
        return "user/guestHome";
    }
}
