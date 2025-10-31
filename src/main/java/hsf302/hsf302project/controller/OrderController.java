package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.OrderDetailEntity;
import hsf302.hsf302project.entity.OrderEntity;
import hsf302.hsf302project.repository.OrderDetailRepository;
import hsf302.hsf302project.repository.ProductRepository;
import hsf302.hsf302project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/listOrders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders",orderService.findAll());
        return "order/orderList";
    }

    @GetMapping("/addPage")
    public String addPage(Model model){
        model.addAttribute("order", new OrderEntity());
        model.addAttribute("orderDetail", new OrderDetailEntity());
        model.addAttribute("products",productRepository.findAll());
        return "order/addOrder";
    }

    @PostMapping("addExecute")
    @Transactional
    public String createOrder(@Valid @ModelAttribute("orders") OrderEntity order,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              BindingResult result) {
        if(result.hasErrors()){
            model.addAttribute("order",order);
            return "order/addOrder";
        }
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

    @GetMapping("updatePage/{id}")
    public String updatePage(@PathVariable int id, Model model){
        model.addAttribute("order",orderService.findById(id));
        model.addAttribute("products",productRepository.findAll());
        return "order/updateOrder";
    }

    @PostMapping("/updateExecute")
    public String update(@Valid @ModelAttribute("order") OrderEntity orderEntity,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("order",orderEntity);
            return "order/addOrder";
        }
        boolean isAdded= orderService.update(orderEntity.getOrderID(),orderEntity);
        if(isAdded){
            redirectAttributes.addFlashAttribute("message","Order updated successfully");
            return "redirect:/orders/listOrders";
        }else{
            model.addAttribute("error","Failed to update order");
            return "order/updateOrder";
        }
    }
}
