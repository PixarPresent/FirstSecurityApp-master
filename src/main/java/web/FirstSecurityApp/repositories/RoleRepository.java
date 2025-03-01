package web.FirstSecurityApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.FirstSecurityApp.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByName(String name);
}
