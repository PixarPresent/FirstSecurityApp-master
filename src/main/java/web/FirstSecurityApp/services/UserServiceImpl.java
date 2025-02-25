package web.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.FirstSecurityApp.dto.UserDTO;
import web.FirstSecurityApp.mappers.UserMapper;
import web.FirstSecurityApp.models.Role;
import web.FirstSecurityApp.models.User;
import web.FirstSecurityApp.repositories.RoleRepository;
import web.FirstSecurityApp.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
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
    public void updateUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));

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
    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDTO).orElse(null);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}