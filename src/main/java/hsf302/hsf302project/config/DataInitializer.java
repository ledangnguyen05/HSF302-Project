package hsf302.hsf302project.config;

import hsf302.hsf302project.entity.RoleEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.repository.RoleRepository;
import hsf302.hsf302project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        // Ensure roles exist
        RoleEntity adminRole = roleRepository.findByRoleNameIgnoreCase("ADMIN")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().roleName("ADMIN").build()));
        RoleEntity staffRole = roleRepository.findByRoleNameIgnoreCase("STAFF")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().roleName("STAFF").build()));
        RoleEntity customerRole = roleRepository.findByRoleNameIgnoreCase("CUSTOMER")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().roleName("CUSTOMER").build()));

        // Seed admin
        userRepository.findByUsernameIgnoreCase("admin").orElseGet(() ->
                userRepository.save(UserEntity.builder()
                        .username("admin")
                        .password("123456")
                        .fullName("System Administrator")
                        .email("admin@example.com")
                        .phone("0900000001")
                        .address("Head Office")
                        .role(adminRole)
                        .build())
        );

        // Seed staff
        userRepository.findByUsernameIgnoreCase("staff").orElseGet(() ->
                userRepository.save(UserEntity.builder()
                        .username("staff")
                        .password("123456")
                        .fullName("Staff User")
                        .email("staff@example.com")
                        .phone("0900000002")
                        .address("Branch A")
                        .role(staffRole)
                        .build())
        );

        // Seed customer
        userRepository.findByUsernameIgnoreCase("customer").orElseGet(() ->
                userRepository.save(UserEntity.builder()
                        .username("customer")
                        .password("123456")
                        .fullName("Customer User")
                        .email("customer@example.com")
                        .phone("0900000003")
                        .address("District 1")
                        .role(customerRole)
                        .build())
        );
    }
}


