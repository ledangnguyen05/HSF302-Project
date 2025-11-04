package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    java.util.Optional<RoleEntity> findByRoleNameIgnoreCase(String roleName);

    RoleEntity findByRoleName(String roleName);
}
