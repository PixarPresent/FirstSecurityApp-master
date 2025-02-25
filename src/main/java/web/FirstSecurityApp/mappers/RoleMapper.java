package web.FirstSecurityApp.mappers;

import org.springframework.stereotype.Component;
import web.FirstSecurityApp.dto.RoleDTO;
import web.FirstSecurityApp.models.Role;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }
}