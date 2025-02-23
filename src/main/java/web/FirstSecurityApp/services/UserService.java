package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {
    void createUser(User user);

    void updateUser(User user);
    void deleteUserById(Long id);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();


}
