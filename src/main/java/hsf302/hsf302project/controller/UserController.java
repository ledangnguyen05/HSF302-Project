package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.RoleEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.repository.RoleRepository;
import hsf302.hsf302project.repository.UserRepository;
import hsf302.hsf302project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/listUsers")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/userList";
    }

    @GetMapping("/registerPage")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("roleList", roleRepository.findAll());
        return "user/register";
    }

    @PostMapping("/registerExecute")
    public String register(@Valid @ModelAttribute("user") UserEntity userEntity,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/register";
        }

        if (userRepository.findByUsernameIgnoreCase(userEntity.getUsername()).isPresent()) {
            model.addAttribute("error", "Username is already taken");
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/register";
        }
        if (userRepository.findByEmailIgnoreCase(userEntity.getEmail()).isPresent()) {
            model.addAttribute("error", "Email is already taken");
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/register";
        }
        if (userRepository.findByPhone(userEntity.getPhone()).isPresent()) {
            model.addAttribute("error", "Phone is already taken");
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/register";
        }
        UserEntity sessionUser = (UserEntity) session.getAttribute("user");

        //Nếu là khách thì chỉ tao dc tai khoan role customer
        if (sessionUser == null) {
            RoleEntity customerRole = roleRepository.findByRoleName("CUSTOMER");
            userEntity.setRole(customerRole);
        }

        boolean success = userService.registerUser(userEntity);
        if (!success) {
            model.addAttribute("error", "Registration failed");
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/register";
        } else {
            UserEntity user=(UserEntity)session.getAttribute("user");
            if(user!=null && (user.getRole().getRoleName().equals("ADMIN") || user.getRole().getRoleName().equals("STAFF"))) {
                redirectAttributes.addFlashAttribute("message", "Registration successful");
                return "redirect:/users/listUsers";
            }else{
                redirectAttributes.addFlashAttribute("message", "Registration successful");
                return "redirect:/login";
            }
        }
    }

    @GetMapping("/updatePage/{id}")
    public String updatePage(@PathVariable int id,
                             Model model) {
        UserEntity user = userService.findByUserId(id);
        model.addAttribute("roleList", roleRepository.findAll());
        model.addAttribute("user", user);
        return "user/updateUser";
    }

    @PostMapping("/updateExecute")
    public String updateExecute(@Valid @ModelAttribute("user") UserEntity userEntity,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/updateUser";
        }

        userRepository.findByUsernameIgnoreCase(userEntity.getUsername())
                .filter(u -> u.getId() != userEntity.getId())
                .ifPresent(u -> {
                    model.addAttribute("error", "Username is already taken");
                    model.addAttribute("roleList", roleRepository.findAll());
                });
        if (model.containsAttribute("error")) return "user/updateUser";

        userRepository.findByEmailIgnoreCase(userEntity.getEmail())
                .filter(u -> u.getId() != userEntity.getId())
                .ifPresent(u -> {
                    model.addAttribute("error", "Email is already taken");
                    model.addAttribute("roleList", roleRepository.findAll());
                });
        if (model.containsAttribute("error")) return "user/updateUser";

        if (userEntity.getPhone() != null && !userEntity.getPhone().isBlank()) {
            userRepository.findByPhone(userEntity.getPhone())
                    .filter(u -> u.getId() != userEntity.getId())
                    .ifPresent(u -> {
                        model.addAttribute("error", "Phone is already taken");
                        model.addAttribute("roleList", roleRepository.findAll());
                    });
            if (model.containsAttribute("error")) return "user/updateUser";
        }

        boolean success = userService.updateUser(userEntity.getId(), userEntity);
        if (!success) {
            model.addAttribute("error", "Update failed due to server error");
            model.addAttribute("roleList", roleRepository.findAll());
            return "user/updateUser";
        } else {
            redirectAttributes.addFlashAttribute("message", "Update successful");
            return "redirect:/users/listUsers";
        }
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id,
                             RedirectAttributes redirectAttributes) {
        boolean success = userService.deleteUser(id);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user");
        }
        return "redirect:/users/listUsers";
    }

    @PostMapping("/search")
    public String search(@RequestParam("searchType") String searchType,
                         @RequestParam("keyword") String keyword,
                         Model model) {

        List<UserEntity> users = new ArrayList<>();

        switch (searchType) {
            case "byUserName":
                users = userService.findByUsername(keyword);
                break;

            case "byMail":
                users = userService.findByEmail(keyword);
                break;

            case "byPhone":
                UserEntity phoneUser = userService.findByPhone(keyword);
                if (phoneUser != null) users.add(phoneUser);
                break;

            case "byRoleName":
                users=userRepository.findByRole_RoleNameIgnoreCase(keyword);
                break;
        }

        if (users.isEmpty()) {
            model.addAttribute("error", "No users found for: " + keyword);
        } else {
            model.addAttribute("users", users);
        }
        model.addAttribute("searchType",searchType);
        return "user/userList";
    }

    @PostMapping("/previousAction")
    public String someAction(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
