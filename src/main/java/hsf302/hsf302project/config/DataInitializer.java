package hsf302.hsf302project.config;

import hsf302.hsf302project.entity.RoleEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.entity.SupplierEntity;
import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.repository.RoleRepository;
import hsf302.hsf302project.repository.UserRepository;
import hsf302.hsf302project.repository.CategoryRepository;
import hsf302.hsf302project.repository.SupplierRepository;
import hsf302.hsf302project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

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

        // Ensure a default Flowers category exists
        CategoryEntity flowersCategory = categoryRepository.findByCategoryNameIgnoreCase("Flowers")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Flowers")
                        .description("All kinds of fresh flowers")
                        .build()));

        CategoryEntity giftsCategory = categoryRepository.findByCategoryNameIgnoreCase("Gifts")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Gifts")
                        .description("Gift baskets and add-ons for flowers")
                        .build()));

        CategoryEntity orchidsCategory = categoryRepository.findByCategoryNameIgnoreCase("Orchids")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Orchids")
                        .description("Potted and cut orchids")
                        .build()));

        CategoryEntity rosesCategory = categoryRepository.findByCategoryNameIgnoreCase("Roses")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Roses")
                        .description("Varieties of roses in different colors")
                        .build()));

        CategoryEntity tulipsCategory = categoryRepository.findByCategoryNameIgnoreCase("Tulips")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Tulips")
                        .description("Seasonal tulips bundles and bouquets")
                        .build()));

        // Ensure a default supplier exists
        SupplierEntity defaultSupplier = supplierRepository.findBySupplierNameIgnoreCase("Fresh Bloom Co.")
                .orElseGet(() -> supplierRepository.save(SupplierEntity.builder()
                        .supplierName("Fresh Bloom Co.")
                        .contactName("Anna Nguyen")
                        .phone("0901234567")
                        .address("123 Flower Street, District 3")
                        .build()));

        SupplierEntity gardenHouse = supplierRepository.findBySupplierNameIgnoreCase("Garden House Ltd.")
                .orElseGet(() -> supplierRepository.save(SupplierEntity.builder()
                        .supplierName("Garden House Ltd.")
                        .contactName("Tran Minh")
                        .phone("0907654321")
                        .address("45 Green Lane, District 7")
                        .build()));

        SupplierEntity orchidWorld = supplierRepository.findBySupplierNameIgnoreCase("Orchid World")
                .orElseGet(() -> supplierRepository.save(SupplierEntity.builder()
                        .supplierName("Orchid World")
                        .contactName("Le Hoa")
                        .phone("0912345678")
                        .address("88 Orchid Ave, Thu Duc City")
                        .build()));

        SupplierEntity sunnyFarms = supplierRepository.findBySupplierNameIgnoreCase("Sunny Farms")
                .orElseGet(() -> supplierRepository.save(SupplierEntity.builder()
                        .supplierName("Sunny Farms")
                        .contactName("Pham Quang")
                        .phone("0988888888")
                        .address("12 Sunshine Road, Binh Thanh")
                        .build()));




        // Seed sample flower products per category & supplier (skip if already present by exact name)
        createProductIfAbsent("Red Rose Bouquet", new BigDecimal("19.99"), 50,
                "Bouquet of 12 premium red roses", rosesCategory, gardenHouse);

        createProductIfAbsent("White Lily Bouquet", new BigDecimal("24.99"), 40,
                "Elegant white lilies, perfect for celebrations", flowersCategory, sunnyFarms);

        createProductIfAbsent("Pink Tulip Bundle", new BigDecimal("14.99"), 60,
                "Bundle of 10 fresh pink tulips", tulipsCategory, sunnyFarms);

        createProductIfAbsent("Purple Orchid Pot", new BigDecimal("29.99"), 30,
                "Potted purple orchid with long-lasting blooms", orchidsCategory, orchidWorld);

        createProductIfAbsent("Sunflower Bunch", new BigDecimal("11.99"), 70,
                "Bright sunflowers to light up any room", flowersCategory, defaultSupplier);
    }

    private void createProductIfAbsent(String name, BigDecimal price, int stock,
                                       String description, CategoryEntity category,
                                       SupplierEntity supplier) {
        boolean exists = productRepository.findByProductNameContainingIgnoreCase(name)
                .stream().anyMatch(p -> p.getProductName().equalsIgnoreCase(name));
        if (!exists) {
            productRepository.save(ProductEntity.builder()
                    .productName(name)
                    .unitPrice(price)
                    .stockQuantity(stock)
                    .description(description)
                    .category(category)
                    .supplier(supplier)
                    .build());
        }
    }
}


