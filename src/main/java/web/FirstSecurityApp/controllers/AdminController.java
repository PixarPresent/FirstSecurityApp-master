package web.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.exceptions.NoSuchElementException;
import web.FirstSecurityApp.exceptions.UserIncorrectData;
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
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    //GET METHOD - GET ALL USERS
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }


    //GET METHOD - FIND USER BY ID
    @GetMapping("/users/{id}")
    public User showUser(@PathVariable("id") long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }

        return user;
    }

    //POST METHOD - CREATE USERS
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        userService.createUser(user);
        return user;

    }

    //PUT METHOD - EDIT USERS
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }


    //DELETE METHOD DELETE USERS
    @DeleteMapping("/users/{id}")
        public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
    }

}
