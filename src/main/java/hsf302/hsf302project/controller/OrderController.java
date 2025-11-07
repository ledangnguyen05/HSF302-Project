package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.OrderDetailEntity;
import hsf302.hsf302project.entity.OrderEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.repository.OrderDetailRepository;
import hsf302.hsf302project.repository.OrderRepository;
import hsf302.hsf302project.repository.ProductRepository;
import hsf302.hsf302project.repository.UserRepository;
import hsf302.hsf302project.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/listOrders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order/orderList";
    }

    @GetMapping("/addPage")
    public String addPage(Model model,
                          HttpSession sesison) {
        OrderEntity order = new OrderEntity();
        order.getOrderDetails().add(new OrderDetailEntity());
        UserEntity user = (UserEntity) sesison.getAttribute("user");
        if (user.getRole().getId() == 3) {
            order.setCustomer(user);
        }
        model.addAttribute("order", order);
        model.addAttribute("products", productRepository.findAll());
        return "order/addOrder";
    }

    @PostMapping("addExecute")
    @Transactional
    public String createOrder(@Valid @ModelAttribute("order") OrderEntity order,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              HttpSession sesison) {
        UserEntity user = (UserEntity) sesison.getAttribute("user");
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            return "order/addOrder";
        }
        Integer customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;

        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Customer ID"));

        order.setCustomer(customer);
        boolean isAdded = orderService.create(order);
        if (isAdded) {
            redirectAttributes.addFlashAttribute("message", "Order added successfully");
            return "redirect:/orders/listOrders";
        } else {
            model.addAttribute("error", "Failed to add order");
            return "order/addOrder";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = orderService.delete(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Order deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete order");
        }
        return "redirect:/orders/listOrders";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable int id) {
        OrderEntity order = orderService.findById(id);
        order.setStatus(OrderEntity.Status.CANCELLED);
        orderService.update(id, order);
        return "redirect:/orders/listOrders";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchType") String searchType,
                         @RequestParam Map<String, String> params,
                         Model model) {

        List<OrderEntity> orders = new ArrayList<>();

        switch (searchType) {
            case "byCustomerName":
                String keyword = params.get("keyword").trim();
                orders = orderRepository.findOrdersByCustomer_fullNameContainingIgnoreCase(keyword);
                break;

            case "byDateRange":
                LocalDate start = LocalDate.parse(params.get("startDate"));
                LocalDate end = LocalDate.parse(params.get("endDate"));

                LocalDateTime startDateTime = start.atStartOfDay();
                LocalDateTime endDateTime = end.atTime(23, 59, 59);
                orders = orderRepository.findOrdersByOrderDateBetween(startDateTime, endDateTime);
                break;

            case "byStatus":
                OrderEntity.Status status = OrderEntity.Status.valueOf(params.get("status"));
                orders = orderRepository.findOrdersByStatus(status);
                break;

            default:
                break;
        }
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @GetMapping("/updatePage/{id}")
    public String updatePage(Model model,
                             HttpSession session,
                             @PathVariable int id) {
        OrderEntity order = orderService.findById(id);
        if (order == null) {
            return "redirect:/orders/listOrders";
        }

        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user.getRole().getId() == 3) {
            order.setCustomer(user);
        }

        model.addAttribute("order", order);
        model.addAttribute("products", productRepository.findAll());
        return "order/updateOrder";
    }


    @PostMapping("/updateExecute")
    @Transactional
    public String updateExecute(@Valid @ModelAttribute("order") OrderEntity order,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("products", productRepository.findAll());
            return "order/updateOrder";
        }

        Integer orderId = order.getOrderID();
        if (orderId == null) {
            redirectAttributes.addFlashAttribute("error", "Order ID missing!");
            return "redirect:/orders/listOrders";
        }

        boolean isUpdated = orderService.update(orderId, order);
        if (isUpdated) {
            redirectAttributes.addFlashAttribute("message", "Order updated successfully!");
            return "redirect:/orders/listOrders";
        } else {
            model.addAttribute("error", "Failed to update order");
            return "order/updateOrder";
        }
    }

}
