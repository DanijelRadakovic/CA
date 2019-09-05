package megatravel.com.pki.repository;

import megatravel.com.pki.domain.rbac.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(String name);

}
