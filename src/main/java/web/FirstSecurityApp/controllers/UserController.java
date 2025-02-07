package web.FirstSecurityApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.FirstSecurityApp.models.User;

@Controller
public class UserController {

    @GetMapping("/user")
    public String showUsersInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/welcome")
    public String welcomePage(Model model) {
        return "welcome";
    }
}
