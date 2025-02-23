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

import java.util.*;

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
    public void createUser(User user) {
        Set<Role> roles = new HashSet<>();

        Role defaultRole = roleRepository.getRoleByName("ROLE_USER");
        roles.add(defaultRole);

        for (Role role : user.getRoles()) {
            Role existingRole = roleRepository.getRoleByName(role.getName());
            roles.add(existingRole);
        }

        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Role> roles = new HashSet<>();

        Role defaultRole = roleRepository.getRoleByName("ROLE_USER");
        roles.add(defaultRole);

        for (Role role : user.getRoles()) {
            Role existingRole = roleRepository.getRoleByName(role.getName());
            roles.add(existingRole);
        }


        existingUser.setRoles(roles);

        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
