package hsf302.hsf302project.controller;


import hsf302.hsf302project.entity.*;
import hsf302.hsf302project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    SupplierService supplierService;

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
                List<OrderEntity>orders=orderService.findAll();
                int countPending=0,countFinished=0,countCancelled=0,customers=0,staffs=0;
                BigDecimal todayRevenue=BigDecimal.ZERO;
                for(OrderEntity order:orders){
                    if(order.getStatus().equals(OrderEntity.Status.PENDING)){
                        countPending++;
                    }
                }
                LocalDate today = LocalDate.now();
                for (OrderEntity order : orders) {
                    if (order.getStatus().equals(OrderEntity.Status.FINISHED)
                            && order.getUpdatedAt() != null
                            && order.getUpdatedAt().toLocalDate().isEqual(today)) {
                        countFinished++;
                    }
                }
                for (OrderEntity order : orders) {
                    if (order.getStatus().equals(OrderEntity.Status.FINISHED)
                            && order.getUpdatedAt() != null
                            && order.getUpdatedAt().toLocalDate().isEqual(today)) {

                        for (OrderDetailEntity detail : order.getOrderDetails()) {
                            if (detail.getSubTotal() != null) {
                                todayRevenue = todayRevenue.add(detail.getSubTotal());
                            }
                        }
                    }
                }
                for (OrderEntity order : orders) {
                    if (order.getStatus().equals(OrderEntity.Status.CANCELLED)
                            && order.getUpdatedAt() != null
                            && order.getUpdatedAt().toLocalDate().isEqual(today)) {
                        countCancelled++;
                    }
                }
                List<UserEntity>users=userService.findAllUsers();
                for(UserEntity user:users){
                    if(user.getRole().getRoleName().equals("CUSTOMER")){
                        customers++;
                    }else if(user.getRole().getRoleName().equals("STAFF")) {
                        staffs++;
                    }
                }
                model.addAttribute("products",productService.getAllProducts());
                model.addAttribute("suppliers",supplierService.getAll());
                model.addAttribute("orders",orders);
                model.addAttribute("todayRevenue",todayRevenue);
                model.addAttribute("countPending",countPending);
                model.addAttribute("countFinished",countFinished);
                model.addAttribute("countCancelled",countCancelled);
                model.addAttribute("countUsers",customers);
                model.addAttribute("countStaffs",staffs);
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
                List<OrderEntity>orders=orderService.findAll();
                int countPending=0,countFinished=0;
                for(OrderEntity order:orders){
                    if(order.getStatus().equals(OrderEntity.Status.PENDING)){
                        countPending++;
                    }
                }
                LocalDate today = LocalDate.now();

                for (OrderEntity order : orders) {
                    if (order.getStatus().equals(OrderEntity.Status.FINISHED)
                            && order.getUpdatedAt() != null
                            && order.getUpdatedAt().toLocalDate().isEqual(today)) {
                        countFinished++;
                    }
                }

                model.addAttribute("orders",orders);
                model.addAttribute("countPending",countPending);
                model.addAttribute("countFinished",countFinished);
                return "user/staffHome";
            }
        }
        return "user/guestHome";
    }
}
