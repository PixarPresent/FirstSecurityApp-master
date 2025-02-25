package web.FirstSecurityApp.services;

import web.FirstSecurityApp.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRolesByIds(List<Long> ids);

    List<Role> getAllRoles();

    Role getRoleById(Long roleId);

    Role getRoleByName(String role);
}
