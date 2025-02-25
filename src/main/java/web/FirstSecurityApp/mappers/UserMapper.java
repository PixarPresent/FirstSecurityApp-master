package web.FirstSecurityApp.mappers;

import org.springframework.stereotype.Component;
import web.FirstSecurityApp.dto.UserDTO;
import web.FirstSecurityApp.models.User;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toSet()));
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles().stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet()));
        return user;
    }
}