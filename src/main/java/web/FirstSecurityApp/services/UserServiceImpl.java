package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public void createUser(User user) {
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, Role role) {
        User updateduser = getUserById(user.getId());
        updateduser.setUsername(user.getUsername());
        updateduser.setPassword(user.getPassword());
        updateduser.setRoles(user.getRoles());
        updateduser.setEmail(user.getEmail());
        userRepository.save(updateduser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User user = null;
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user, List<Role> roles) {
        user.setRoles(roles);
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }
}
