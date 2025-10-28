package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.UserEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByUsernameAndPassword(String username, String password);
    List<UserEntity> findByUsernameContainingIgnoreCase(String username);
    List<UserEntity> findByEmailContainingIgnoreCase(String email);
    List<UserEntity> findByRole_RoleNameIgnoreCase(String roleRoleName);
    Optional<UserEntity> findByUsernameIgnoreCase(String username);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByPhone(String phone);

    Optional<Object> findByEmailContainingIgnoreCase(String email, Sort sort, Limit limit);
}
