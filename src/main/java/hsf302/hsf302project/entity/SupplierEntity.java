package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@Entity
@Table(name = "SUPPLIER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SupplierID")
    private int id;

    @NotNull(message = "Supplier name cannot be blank")
    @Size(max=50,message = "Supplier name must not exceed 50 characters")
    @Column(name = "SupplierName", nullable = false)
    private String supplierName;

    @Size(max=100,message = "Contact name can not exceed 100 characters")
    @Column(name = "ContactName", nullable = false)
    private String contactName;

    @NotBlank(message = "Phone cannot blank")
    @Pattern(regexp = "^[0-9]{9,15}$",message = "Phone number must be between 9 and 15 digits")
    @Column(name = "Phone", nullable = false)
    private String phone;

    @Size(max=200,message = "Address can not exceed 200 characters")
    @Column(name = "Address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<ProductEntity> products = new ArrayList<>();
}
