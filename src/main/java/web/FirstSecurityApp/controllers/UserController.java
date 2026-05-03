package web.FirstSecurityApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.FirstSecurityApp.dto.UserDTO;
import web.FirstSecurityApp.services.UserService;
import web.FirstSecurityApp.services.impl.UserNotFoundException;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserDTO> getUserById(Principal principal) throws UserNotFoundException {
        String username = principal.getName();
        UserDTO userDTO = userService.getUserByUsername(username);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/avatar/upload")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file, Principal principal) {
        String username = principal.getName();
//        avatarService.storeAvatar(file, username);
        return ResponseEntity.ok("Upload successful");
    }
}