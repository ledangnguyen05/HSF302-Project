package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data
@Entity
@Table(name = "ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private int id;

    @Column(name = "RoleName", nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserEntity> users = new ArrayList<>();
}
