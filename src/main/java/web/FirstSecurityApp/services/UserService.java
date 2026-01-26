package web.FirstSecurityApp.services;

import web.FirstSecurityApp.dto.UserDTO;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.services.impl.UserNotFoundException;

import java.util.List;

public interface UserService {
    void createUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);
    void deleteUserById(Long id);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username) throws UserNotFoundException;
    List<UserDTO> getAllUsers();


}
