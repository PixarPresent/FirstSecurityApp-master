package web.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.services.UserService;

import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    //TODO.html: сделать запросы на странички

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/adminPages/users";
    }

    @GetMapping("/admin/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "/adminPages/showUser";
    }

//    @GetMapping("/admin/create")
//    public String createUser(Model model) {
//        model.addAttribute("user", new User());
//        return "/adminPages/createUser";
//    }
//
//    @PostMapping("/admin/create")
//    public String createUser(@ModelAttribute("user") User user) {
//        userService.createUser(user);
//        return "redirect:/admin";
//    }
}
