package web.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.RoleRepository;
import web.FirstSecurityApp.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void createUser(User user, Set<Role> roles) {
        Role userRole = roleRepository.getRoleByName("ROLE_USER");
        if (userRole != null && !roles.contains(userRole)) {
            roles.add(userRole);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setUsername(user.getUsername());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User updatedUser = getUserById(user.getId());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setRoles(user.getRoles());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            updatedUser.setPassword(user.getPassword());
        }

        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
