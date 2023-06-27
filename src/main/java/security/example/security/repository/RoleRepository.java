package security.example.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.example.security.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String role);
}
