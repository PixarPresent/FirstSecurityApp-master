package web.FirstSecurityApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    public String showUserInfo(Model model) {
        return "user";
    }

    @GetMapping("/welcome")
    public String welcomePage(Model model) {
        return "welcome";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }
}
