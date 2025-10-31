package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "IMPORT_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ImportDetailID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReceiptID", nullable = false)
    private ImportReceiptEntity receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @DecimalMin(value = "0.00", message = "Unit cost must be positive")
    @Column(name = "UnitCost", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitCost;

    @DecimalMin(value = "0.00", message = "Subtotal must be positive")
    @Column(name = "SubTotal", precision = 12, scale = 2, nullable = false)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @PrePersist
    @PreUpdate
    public void calculateSubTotal() {
        if (unitCost != null && quantity > 0) {
            this.subTotal = unitCost.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subTotal = BigDecimal.ZERO;
        }
    }
}
