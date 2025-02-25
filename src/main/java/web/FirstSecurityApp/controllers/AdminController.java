package web.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.FirstSecurityApp.dto.UserDTO;
import web.FirstSecurityApp.exceptions.NoSuchElementException;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.services.RoleService;
import web.FirstSecurityApp.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/admin")
    public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

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





