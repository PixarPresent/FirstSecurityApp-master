package web.FirstSecurityApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //GET METHOD - GET USER BY ID
    @GetMapping()
    public ResponseEntity<User> showUser(Principal principal) {
        String username = principal.getName();
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }
}
