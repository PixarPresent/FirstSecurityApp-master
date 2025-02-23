package web.FirstSecurityApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.FirstSecurityApp.models.User;


@Controller
public class ViewController {

    @GetMapping("/admin")
    public String adminPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}