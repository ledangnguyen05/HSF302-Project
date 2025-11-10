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

        // Ensure categories exist: Rose, Sunflower, Hibiscus, Chrysanthemum, Tulip
        CategoryEntity roseCategory = categoryRepository.findByCategoryNameIgnoreCase("Rose")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Rose")
                        .description("Varieties of roses in different colors")
                        .build()));

        CategoryEntity sunflowerCategory = categoryRepository.findByCategoryNameIgnoreCase("Sunflower")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Sunflower")
                        .description("Bright and cheerful sunflowers")
                        .build()));

        CategoryEntity hibiscusCategory = categoryRepository.findByCategoryNameIgnoreCase("Hibiscus")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Hibiscus")
                        .description("Tropical hibiscus flowers")
                        .build()));

        CategoryEntity chrysanthemumCategory = categoryRepository.findByCategoryNameIgnoreCase("Chrysanthemum")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Chrysanthemum")
                        .description("Beautiful chrysanthemum flowers")
                        .build()));

        CategoryEntity tulipCategory = categoryRepository.findByCategoryNameIgnoreCase("Tulip")
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName("Tulip")
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




        // Seed 5 sample flower products per category (25 products total)
        // Rose category products
        createProductIfAbsent("Red Rose Bouquet", new BigDecimal("180000"), 50,
                "Bouquet of 12 premium red roses","Red_Rose_Bouquet.jpg", roseCategory, gardenHouse);
        createProductIfAbsent("Pink Rose Bouquet", new BigDecimal("180000"), 50,
                "Bouquet of 12 delicate pink roses","Pink_Rose_Bouquet.jpg", roseCategory, gardenHouse);
        createProductIfAbsent("White Rose Bouquet", new BigDecimal("180000"), 50,
                "Bouquet of 12 elegant white roses","White_Rose_Bouquet.jpg", roseCategory, gardenHouse);
        createProductIfAbsent("Yellow Rose Bouquet", new BigDecimal("180000"), 50,
                "Bouquet of 12 bright yellow roses","Yellow_Rose_Bouquet.jpg", roseCategory, gardenHouse);
        createProductIfAbsent("Mixed Rose Bouquet", new BigDecimal("220000"), 40,
                "Bouquet of 12 mixed color roses","Mixed_Rose_Bouquet.jpg", roseCategory, gardenHouse);

        // Sunflower category products
        createProductIfAbsent("Giant Sunflower", new BigDecimal("120000"), 60,
                "Large giant sunflower with tall stem","Giant_Sunflower.jpg", sunflowerCategory, sunnyFarms);
        createProductIfAbsent("Dwarf Sunflower", new BigDecimal("100000"), 70,
                "Compact dwarf sunflower perfect for decoration","Dwarf_Sunflower.jpg", sunflowerCategory, sunnyFarms);
        createProductIfAbsent("Red Sunflower", new BigDecimal("130000"), 50,
                "Unique red sunflower with vibrant color","Red_Sunflower.jpg", sunflowerCategory, sunnyFarms);
        createProductIfAbsent("Lemon Queen Sunflower", new BigDecimal("115000"), 55,
                "Beautiful lemon-colored sunflower","Lemon_Queen_Sunflower.jpg", sunflowerCategory, sunnyFarms);
        createProductIfAbsent("Teddy Bear Sunflower", new BigDecimal("120000"), 50,
                "Fluffy double-petaled sunflower","Teddy_Bear_Sunflower.jpg", sunflowerCategory, sunnyFarms);

        // Hibiscus category products
        createProductIfAbsent("Red Hibiscus", new BigDecimal("140000"), 45,
                "Vibrant red hibiscus flower","Red_Hibiscus.jpg", hibiscusCategory, orchidWorld);
        createProductIfAbsent("Pink Hibiscus", new BigDecimal("140000"), 45,
                "Beautiful pink hibiscus flower","Pink_Hibiscus.jpg", hibiscusCategory, orchidWorld);
        createProductIfAbsent("Yellow Hibiscus", new BigDecimal("140000"), 45,
                "Bright yellow hibiscus flower","Yellow_Hibiscus.jpg", hibiscusCategory, orchidWorld);
        createProductIfAbsent("White Hibiscus", new BigDecimal("150000"), 40,
                "Elegant white hibiscus flower","White_Hibiscus.jpg", hibiscusCategory, orchidWorld);
        createProductIfAbsent("Tropical Hibiscus Mix", new BigDecimal("170000"), 35,
                "Mixed tropical hibiscus bouquet","Tropical_Hibiscus_Mix.jpg", hibiscusCategory, orchidWorld);

        // Chrysanthemum category products
        createProductIfAbsent("White Chrysanthemum", new BigDecimal("130000"), 55,
                "Pure white chrysanthemum bouquet","White_Chrysanthemum.jpg", chrysanthemumCategory, defaultSupplier);
        createProductIfAbsent("Yellow Chrysanthemum", new BigDecimal("130000"), 55,
                "Golden yellow chrysanthemum bouquet","Yellow_Chrysanthemum.jpg", chrysanthemumCategory, defaultSupplier);
        createProductIfAbsent("Purple Chrysanthemum", new BigDecimal("135000"), 50,
                "Rich purple chrysanthemum bouquet","Purple_Chrysanthemum.jpg", chrysanthemumCategory, defaultSupplier);
        createProductIfAbsent("Pink Chrysanthemum", new BigDecimal("130000"), 55,
                "Soft pink chrysanthemum bouquet","Pink_Chrysanthemum.jpg", chrysanthemumCategory, defaultSupplier);
        createProductIfAbsent("Mixed Chrysanthemum", new BigDecimal("150000"), 45,
                "Colorful mixed chrysanthemum bouquet","Mixed_Chrysanthemum.jpg", chrysanthemumCategory, defaultSupplier);

        // Tulip category products
        createProductIfAbsent("Red Tulip Bundle", new BigDecimal("110000"), 65,
                "Bundle of 10 classic red tulips","Red_Tulip_Bundle.jpg", tulipCategory, sunnyFarms);
        createProductIfAbsent("Yellow Tulip Bundle", new BigDecimal("110000"), 65,
                "Bundle of 10 bright yellow tulips","Yellow_Tulip_Bundle.jpg", tulipCategory, sunnyFarms);
        createProductIfAbsent("Pink Tulip Bundle", new BigDecimal("110000"), 65,
                "Bundle of 10 delicate pink tulips","Pink_Tulip_Bundle.jpg", tulipCategory, sunnyFarms);
        createProductIfAbsent("Purple Tulip Bundle", new BigDecimal("120000"), 60,
                "Bundle of 10 elegant purple tulips","Purple_Tulip_Bundle.jpg", tulipCategory, sunnyFarms);
        createProductIfAbsent("Rainbow Tulip Mix", new BigDecimal("150000"), 50,
                "Colorful rainbow tulip bouquet","Rainbow_Tulip_Mix.jpg", tulipCategory, sunnyFarms);
    }

    private void createProductIfAbsent(String name, BigDecimal price, int stock,
                                       String description,String image, CategoryEntity category,
                                       SupplierEntity supplier) {
        boolean exists = productRepository.findByProductNameContainingIgnoreCase(name)
                .stream().anyMatch(p -> p.getProductName().equalsIgnoreCase(name));
        if (!exists) {
            productRepository.save(ProductEntity.builder()
                    .productName(name)
                    .unitPrice(price)
                    .stockQuantity(stock)
                    .description(description)
                    .imagePath(image)
                    .category(category)
                    .supplier(supplier)
                    .build());
        }
    }
}


