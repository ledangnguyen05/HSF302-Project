package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private int id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Column(name = "Username", nullable = false, unique = true, length = 20)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    @Column(name = "PasswordHash", nullable = false, length = 64)
    private String password;

    @Size(max = 50, message = "Full name must not exceed 50 characters")
    @Column(name = "FullName", length = 50)
    private String fullName;

    @Email(message = "Invalid email format")
    @Column(name = "Email")
    private String email;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Phone number must be between 9 and 15 digits")
    @Column(name = "Phone", nullable = false, unique = true)
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "IsActive", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "CreatedAt", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "RoleID", nullable = false)
    private RoleEntity role;
}
