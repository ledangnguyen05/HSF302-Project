package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ORDER_DETAIL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailEntity {

    @EmbeddedId
    private OrderDetailKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "OrderID", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;


    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "UnitPrice", nullable = false)
    private BigDecimal unitPrice;
}
