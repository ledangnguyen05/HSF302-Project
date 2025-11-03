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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;

    @Column(name = "UnitPrice",nullable = true)
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Integer quantity = 1;

    @DecimalMin(value = "0.00", message = "Subtotal must be positive")
    @Column(name = "SubTotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @PrePersist
    @PreUpdate
    public void calculateSubTotal() {
        if (quantity > 0 && product != null && product.getUnitPrice() != null) {
            this.subTotal = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
            this.unitPrice = product.getUnitPrice();
        } else {
            this.subTotal = BigDecimal.ZERO;
        }
    }
}