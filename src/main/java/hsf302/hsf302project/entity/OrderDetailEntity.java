package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ORDER_DETAIL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderDetailID")
    private int id;
    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false)
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;
    @NotNull
    @DecimalMin(value = "0.00", message = "Unit price must be positive")
    @Column(name = "UnitPrice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    @NotNull
    @Column(name = "Quantity", nullable = false)
    private int quantity;
    @DecimalMin(value = "0.00", message = "Subtotal must be positive")
    @Column(name = "SubTotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @PrePersist
    @PreUpdate
    public void calculateSubTotal() {
        if (unitPrice != null && quantity > 0) {
            this.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subTotal = BigDecimal.ZERO;
        }
    }
}