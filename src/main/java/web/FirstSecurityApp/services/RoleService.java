package web.FirstSecurityApp.services;

import org.springframework.stereotype.Service;
import web.FirstSecurityApp.models.Role;

import java.util.List;

@Service
public interface RoleService {
    List<Role> getRolesByIds(List<Long> ids);

    List<Role> getAllRoles();
}
