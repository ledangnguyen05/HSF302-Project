package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.OrderDetailEntity;
import hsf302.hsf302project.entity.OrderEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.repository.OrderDetailRepository;
import hsf302.hsf302project.repository.ProductRepository;
import hsf302.hsf302project.repository.UserRepository;
import hsf302.hsf302project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    @GetMapping("/listOrders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders",orderService.findAll());
        return "order/orderList";
    }

    @GetMapping("/addPage")
    public String addPage(Model model){
        OrderEntity order = new OrderEntity();
        order.getOrderDetails().add(new OrderDetailEntity());

        model.addAttribute("order", order);
        model.addAttribute("products",productRepository.findAll());
        return "order/addOrder";
    }

    @PostMapping("addExecute")
    @Transactional
    public String createOrder(@Valid @ModelAttribute("order") OrderEntity order,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              BindingResult result) {
        if(result.hasErrors()){
            model.addAttribute("order",order);
            return "order/addOrder";
        }
        Integer customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;

        if(customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Customer ID"));

        order.setCustomer(customer);
        boolean isAdded= orderService.create(order);
        if(isAdded){
            redirectAttributes.addFlashAttribute("message","Order added successfully");
            return "redirect:/orders/listOrders";
        }else{
            model.addAttribute("error","Failed to add order");
            return "order/addOrder";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes){
        boolean isDeleted= orderService.delete(id);
        if(isDeleted){
            redirectAttributes.addFlashAttribute("message","Order deleted successfully");
        }else{
            redirectAttributes.addFlashAttribute("error","Failed to delete order");
        }
        return "redirect:/orders/listOrders";
    }

}
