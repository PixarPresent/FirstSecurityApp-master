package web.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.services.RoleService;
import web.FirstSecurityApp.services.UserService;

import java.util.*;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

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

    @GetMapping("/admin/create")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.getAllRoles());
        return "/adminPages/createUser";
    }

    @PostMapping("/admin/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam(value="roles", required = false) List<Long> roles) {
        Set<Role> userRoles = new HashSet<>();

        Role userRole = roleService.getRoleByName("ROLE_USER");
        if (userRole != null) {
            userRoles.add(userRole);
        }

        if (roles != null) {
            for (Long roleId : roles) {
                Role role = roleService.getRoleById(roleId);
                if (role != null) {
                    userRoles.add(role);
                }
            }
        }

        userService.createUser(user, userRoles);

        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);

        List<Role> roleList = roleService.getAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);

        return "/adminPages/editUser";
    }

    @PostMapping("/admin/edit")
    public String updateUser(
            @ModelAttribute("user") User user,
            @RequestParam(value = "roles", required = false) List<Long> roles,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            Model model  // Добавьте Model для отображения ошибок
    ) {

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("passwordMismatch", "Пароли не совпадают.");
                model.addAttribute("allRoles", roleService.getAllRoles());
                return "adminPages/editUser";
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }


        Set<Role> userRoles = new HashSet<>();

        Role userRole = roleService.getRoleByName("ROLE_USER");
        if (userRole != null) {
            userRoles.add(userRole);
        }

        if (roles != null) {
            for (Long roleId : roles) {
                Role role = roleService.getRoleById(roleId);
                if (role != null) {
                    userRoles.add(role);
                }
            }
        }

        user.setRoles(userRoles);
        userService.updateUser(user);

        return "redirect:/admin";
    }


    @PostMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }
}
