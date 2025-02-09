package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;

import java.util.List;

@Service
public interface UserService {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();


}
