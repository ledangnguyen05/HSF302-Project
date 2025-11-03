package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.CartEntity;
import hsf302.hsf302project.repository.CartRepository;
import hsf302.hsf302project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/listCarts")
    public String listCartPage(Model model) {
        model.addAttribute("cartList", cartService.findAllCarts());
        return "cart/cartList";
    }

    @PostMapping("/updateQuantity/{id}")
    public String updateQuantityForm(@PathVariable Long id,
                                     @RequestParam int quantity,
                                     RedirectAttributes redirectAttributes) {
        if (quantity < 1) quantity = 1;
        cartService.updateQuantity(id, quantity);
        return "redirect:/carts/listCarts";
    }

    @GetMapping("/deleteCart/{id}")
    public String deleteCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = cartService.deleteCart(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Cart deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete cart");
        }
        return "redirect:/carts/listCarts";
    }
}
