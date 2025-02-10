package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {
    void createUser(User user, Set<Role> roles);
    void updateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();


}
