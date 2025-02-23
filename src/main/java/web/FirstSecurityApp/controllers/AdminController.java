package web.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.exceptions.NoSuchElementException;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.services.RoleService;
import web.FirstSecurityApp.services.UserService;

import java.util.*;

//TODO: рест контроллеры

@RestController
@RequestMapping("/api")
public class AdminController {
    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, BCryptPasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }


    //GET METHOD - GET ALL USERS
    @GetMapping("/admin")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    //GET METHOD - GET USER BY ID
    @GetMapping("/admin/{id}")
    public ResponseEntity<User> showUser(@PathVariable("id") long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //POST METHOD - CREATE USERS
    @PostMapping("/admin")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //PUT METHOD - EDIT USERS
    @PutMapping("/admin")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    //DELETE METHOD DELETE USERS
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

}
